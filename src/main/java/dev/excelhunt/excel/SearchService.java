package dev.excelhunt.excel;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Map<String, Object>> multiCollectionSearch(String searchText) {

        String normalizedSearchText = normalize(searchText);

        List<String> collections = Arrays.asList("儲位資料", "月庫存", "毛量", "產品資料");
        Map<String, Map<String, Object>> mergedResultsMap = new LinkedHashMap<>();

        for (String collection : collections) {
        long collectionStartTime = System.currentTimeMillis();
            Set<String> fieldNames = getAllFieldNamesExceptId(collection);
            Criteria criteria = new Criteria();
            List<Criteria> orCriteriaList = new ArrayList<>();

            List<Criteria> orCriterias = new ArrayList<>();
            for (String fieldName : fieldNames) {
                orCriteriaList.add(Criteria.where(fieldName).regex(".*" + Pattern.quote(normalizedSearchText) + ".*", "i"));
            }


            if (!orCriterias.isEmpty()) {
                criteria.orOperator(orCriterias.toArray(new Criteria[0]));
            }

            Query query = new Query(criteria);

            List<Map> rawResults = mongoTemplate.find(query, Map.class, collection);
            postProcessResults(rawResults);
            List<Map<String, Object>> results = new ArrayList<>();

            for (Map rawResult : rawResults) {
                results.add((Map<String, Object>) rawResult);
            }

            results.removeIf(map -> map.values().contains("NaN"));  // Remove entries with value "NAN"

            mergedResultsMap = mergeResults(mergedResultsMap, results, searchText);
        long collectionEndTime = System.currentTimeMillis();
        System.out.println("Search time for collection '" + collection + "': " + (collectionEndTime - collectionStartTime) + "ms");
        }
        return new ArrayList<>(mergedResultsMap.values());
    }


    private String normalize(String searchText) {

        return searchText.replaceAll("[>.]", "").toLowerCase();
    }

    private Map<String, Map<String, Object>> mergeResults(Map<String, Map<String, Object>> mergedResultsMap, List<Map<String, Object>> results, String searchText) {
        for (Map<String, Object> result : results) {
            // 如果當前結果包含搜索文本，則合併結果
            if (result.values().stream().anyMatch(val -> val != null && val.toString().contains(searchText))) {
                String matchKey = result.values().stream()
                        .filter(val -> val != null && val.toString().contains(searchText))
                        .findFirst()
                        .orElse(null)
                        .toString();

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

    public void saveSearchRecord(String query, String userId) {
        Map<String, Object> searchRecord = new HashMap<>();
        searchRecord.put("query", query);
        searchRecord.put("userId", userId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String dateStr = sdf.format(new Date());

        searchRecord.put("timestamp", dateStr);
        mongoTemplate.save(searchRecord, "SearchRecords");
    }
    private Set<String> getAllFieldNamesExceptId(String collectionName) {
        Set<String> fieldNames = new HashSet<>();

        // Fetch the first document to get the field names
        Document firstDoc = mongoTemplate.getCollection(collectionName).find().first();
        if (firstDoc != null) {
            for (String key : firstDoc.keySet()) {
                if (!key.equals("_id")) {
                    fieldNames.add(key);
                }
            }
        }
        return fieldNames;
    }

    private void postProcessResults(List<Map> results) {
        for (Map<String, Object> result : results) {
            result.remove("_id");  // 移除 _id 字段

            // 移除值為 NaN 的字段
            result.entrySet().removeIf(entry -> "NaN".equals(String.valueOf(entry.getValue())));
        }
    }

}
