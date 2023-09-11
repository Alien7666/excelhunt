package dev.excelhunt.excel;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SearchServicemuticore {

    @Autowired
    private MongoTemplate mongoTemplate;
    private ExecutorService executorService;

    Map<String, String> thisCollectionTime = new HashMap<>();


    public List<String> getCollections() {
        Set<String> allCollections = mongoTemplate.getCollectionNames();
        allCollections.remove("Settings");
        allCollections.remove("searchHistory");
        allCollections.remove("user");
        return new ArrayList<>(allCollections);
    }



    public SearchServicemuticore() {
        this.executorService = Executors.newFixedThreadPool(2);  // 假設有4個集合，因此使用4個線程。
    }

    public List<Map<String, Object>> multiCollectionSearch(String searchText) throws InterruptedException, ExecutionException {
        String normalizedSearchText = normalize(searchText);
        List<String> collections = getSelectedCollections();
        Map<String, Map<String, Object>> mergedResultsMap = new LinkedHashMap<>();
        List<Future<List<Map<String, Object>>>> futures = new ArrayList<>();

        for (String collection : collections) {
            Future<List<Map<String, Object>>> future = executorService.submit(() -> searchCollection(collection, normalizedSearchText));
            futures.add(future);
        }

        for (Future<List<Map<String, Object>>> future : futures) {
            List<Map<String, Object>> results = future.get();
            mergedResultsMap = mergeResults(mergedResultsMap, results, normalizedSearchText);
        }

        List<Map<String, Object>> filteredResults = filterBySelectedFields(mergedResultsMap);
        return filterDuplicates(filteredResults);
    }
    private List<Map<String, Object>> filterBySelectedFields(Map<String, Map<String, Object>> mergedResultsMap) {
        List<String> selectedFields = getSelectedFields();
        return mergedResultsMap.values().stream()
                .map(record -> {
                    return record.entrySet().stream()
                            .filter(entry -> selectedFields.contains(entry.getKey()))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                })
                .collect(Collectors.toList());
    }

    private Map<String, Map<String, Object>> mergeResults(Map<String, Map<String, Object>> mergedResultsMap, List<Map<String, Object>> results, String normalizedSearchText) {
        for (Map<String, Object> result : results) {
            // 如果當前結果包含搜索文本，則合併結果
            if (result.values().stream().anyMatch(val -> val != null && val.toString().toLowerCase().contains(normalizedSearchText))) {
                Optional<Object> matchValue = result.values().stream()
                        .filter(val -> val != null && val.toString().toLowerCase().contains(normalizedSearchText))
                        .findFirst();

                String matchKey = null;
                if (matchValue.isPresent()) {
                    matchKey = matchValue.get().toString();
                }



                // 如果找到匹配的鍵，則使用它來合併或創建新條目
                if (matchKey != null) {
                    if (!mergedResultsMap.containsKey(matchKey)) {
                        mergedResultsMap.put(matchKey, new LinkedHashMap<>());
                    }
                    mergedResultsMap.get(matchKey).putAll(result);
                }
            }
        }
        return mergedResultsMap;
    }

    private List<Map<String, Object>> searchCollection(String collection, String normalizedSearchText) {
        long collectionStartTime = System.currentTimeMillis();
        Set<String> fieldNames = getFieldsForCollection(collection);
        Criteria criteria = new Criteria();
        List<Criteria> orCriteriaList = new ArrayList<>();

        for (String fieldName : fieldNames) {
            String regexSearchText = normalizedSearchText.replaceAll("", ".*"); // 在每個字符間插入".*"
            orCriteriaList.add(Criteria.where(fieldName).regex(regexSearchText, "i"));
        }


        if (!orCriteriaList.isEmpty()) {
            criteria.orOperator(orCriteriaList.toArray(new Criteria[0]));
        }

        Query query = new Query(criteria);

        List<Map> rawResults = mongoTemplate.find(query, Map.class, collection);
        postProcessResults(rawResults);
        List<Map<String, Object>> results = new ArrayList<>();

        for (Map rawResult : rawResults) {
            results.add((Map<String, Object>) rawResult);
        }

        results.removeIf(map -> map.values().contains("NaN"));

        long collectionEndTime = System.currentTimeMillis();
        String timeValue = "搜尋" + collection + "的時間為: " + (collectionEndTime - collectionStartTime) + "ms";
        thisCollectionTime.put(collection, timeValue);
        return results;
    }


    private void postProcessResults(List<Map> results) {
        for (Map<String, Object> result : results) {
            result.remove("_id");  // 移除 _id 字段

            // 移除值為 NaN 的字段
            result.entrySet().removeIf(entry -> "NaN".equals(String.valueOf(entry.getValue())));
        }
    }

    public Set<String> getFieldsForCollection(String collection) {
        Set<String> fields = new HashSet<>();
        Map<String, Object> firstEntry = mongoTemplate.findOne(new Query().limit(1), Map.class, collection);

        if (firstEntry != null) {
            fields.addAll(firstEntry.keySet());
        }

        fields.remove("_id");  // Exclude _id field
        return fields;
    }

    private String normalize(String searchText) {
        return searchText.replaceAll("[>.]", "").toLowerCase();
    }

    public void saveSearchRecord(String searchText, String userId ,String totalSearchTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        String formattedDate = sdf.format(new Date());

        Query query = new Query();
        query.addCriteria(Criteria.where("searchText").is(searchText));

        Map<String, Object> searchRecord = new HashMap<>();
        searchRecord.put("searchText", searchText);
        searchRecord.put("userId", userId);
        searchRecord.put("timestamp", formattedDate);

        for(Map.Entry<String, String> entry : thisCollectionTime.entrySet()){
            String collectionName = entry.getKey();
            String timeValue = entry.getValue();
            searchRecord.put(collectionName, timeValue);
        }

        searchRecord.put("totalSearchTime", totalSearchTime);

        mongoTemplate.insert(searchRecord, "searchHistory");
    }
    // 新增的方法，用於過濾重複的字段值
    private List<Map<String, Object>> filterDuplicates(List<Map<String, Object>> mergedResults) {
        Set<String> seenValues = new HashSet<>();
        List<Map<String, Object>> uniqueResults = new ArrayList<>();

        for (Map<String, Object> result : mergedResults) {
            String concatenatedValues = result.values().stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .sorted()
                    .collect(Collectors.joining("|"));

            if (!seenValues.contains(concatenatedValues)) {
                seenValues.add(concatenatedValues);
                uniqueResults.add(result);
            }
        }

        return uniqueResults;
    }
    public void saveSelectedFields(List<String> selectedFields) {
        Map<String, Object> data = new HashMap<>();
        data.put("_id", "selectedFieldsConfig");  // Use a fixed ID
        data.put("selectedFields", selectedFields);
        mongoTemplate.save(data, "Settings");
    }

    public List<String> getSelectedFields() {
        // Retrieve document based on specific _id
        Map<String, Object> data = mongoTemplate.findOne(new Query(Criteria.where("_id").is("selectedFieldsConfig")), Map.class, "Settings");

        if (data == null || !data.containsKey("selectedFields")) {
            return new ArrayList<>();  // Return an empty list if data is null or doesn't contain "selectedFields"
        }

        return (List<String>) data.get("selectedFields");
    }

    // Retrieve the selected collections from the database
    public List<String> getSelectedCollections() {
        Document settings = mongoTemplate.findOne(new Query(Criteria.where("_id").is("selectedCollections")), Document.class, "Settings");
        if (settings != null && settings.containsKey("selectedCollections")) {
            return (List<String>) settings.get("selectedCollections");
        }
        return new ArrayList<>();
    }

    // Save the selected collections to the database
    public void saveSelectedCollections(List<String> selectedCollections) {
        Map<String, Object> data = new HashMap<>();
        data.put("_id", "selectedCollections");  // Use a fixed ID
        data.put("selectedCollections", selectedCollections);
        mongoTemplate.save(data, "Settings");
    }

}
