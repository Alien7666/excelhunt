# ExcelHunt 搜尋應用程式

## 專案概述
ExcelHunt 是一個基於 Spring Boot 的網頁應用程式，專門用於搜尋和管理 Excel 資料。系統透過MongoDB進行資料存儲，支援多集合的並行搜尋，提供高效的資料檢索服務。

## 主要功能
- **快速搜尋**：支援跨多個資料集合的高效搜尋
- **使用者認證**：完整的登入與權限控制系統
- **資料管理**：提供資料維護與更新功能
- **變更記錄**：追蹤系統變更歷史
- **個人設定**：使用者自訂設定

## 技術架構
- **後端框架**：Spring Boot 2.7.15
- **資料庫**：MongoDB
- **前端技術**：Thymeleaf, HTML, CSS, JavaScript
- **安全框架**：Spring Security
- **其他工具**：Google Cloud Storage, Project Lombok

## 快速開始

### 系統需求
- Java 17 或更高版本
- Maven 3.6 或更高版本
- MongoDB

### 執行方式
1. 克隆專案到本地
2. 配置資料庫連接（參見 application.properties）
3. 執行以下命令：
   ```
   mvn spring-boot:run
   ```
4. 訪問 http://localhost:8080 

### Docker 部署
專案支援 Docker 部署，請參考 `docker commands.txt` 檔案中的指令。

## 專案結構
- `src/main/java`: Java 原始碼
- `src/main/resources`: 配置檔案與前端資源
- `src/test`: 測試程式碼

## 授權資訊
© 2025 ExcelHunt. 保留所有權利。