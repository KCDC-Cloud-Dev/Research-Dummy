using Google.Apis.Auth.OAuth2;
using Google.Cloud.Firestore;

namespace CUB_GCP_POC1.Service
{
    public class FileStoreService
    {

        private readonly FirestoreDb _db;

        /// <summary>
        /// 建構子。
        /// </summary>
        /// <param name="projectID">GCP 專案 ID。</param>
        /// <param name="gcpAuthFilePath">GCP 認證檔案路徑。</param>
        public FileStoreService(string projectID, string gcpAuthFilePath)
        {

            if (!string.IsNullOrEmpty(gcpAuthFilePath))
            {
                GoogleCredential cred = GoogleCredential.FromFile(gcpAuthFilePath);
                _db = new FirestoreDbBuilder { ProjectId = projectID, Credential = cred }.Build();
            }
            else
            {
                _db = new FirestoreDbBuilder { ProjectId = projectID}.Build();
            }     
        }


        /// <summary>
        /// 新增資料到 Firestore。
        /// </summary>
        /// <param name="collectionName">集合名稱。</param>
        /// <param name="documentId">文檔 ID。</param>
        /// <param name="data">要儲存的資料。</param>
        public async Task CreateData(string collectionName
                                   , string documentId
                                   , Dictionary<string, object> data)
        {
            DocumentReference docRef = _db.Collection(collectionName).Document(documentId);
            await docRef.SetAsync(data);
        }

        /// <summary>
        /// 從 Firestore 讀取資料。
        /// </summary>
        /// <param name="collectionName">集合名稱。</param>
        /// <param name="documentId">文檔 ID。</param>
        /// <returns>文檔的資料。</returns>
        public async Task<Dictionary<string, object>> ReadData(string collectionName
                                                             , string documentId)
        {
            DocumentReference docRef = _db.Collection(collectionName).Document(documentId);
            DocumentSnapshot snapshot = await docRef.GetSnapshotAsync();

            if (snapshot.Exists)
            {
                Dictionary<string, object> data = snapshot.ToDictionary();
                return data;
            }
            else
            {
                return null; 
            }
        }

        /// <summary>
        /// 更新 Firestore 中的資料。
        /// </summary>
        /// <param name="collectionName">集合名稱。</param>
        /// <param name="documentId">文檔 ID。</param>
        /// <param name="data">要更新的資料。</param>
        public async Task UpdateData(string collectionName
                                   , string documentId
                                   , Dictionary<string, object> data)
        {
            DocumentReference docRef = _db.Collection(collectionName).Document(documentId);
            await docRef.UpdateAsync(data);
        }


        /// <summary>
        /// 從 Firestore 刪除資料。
        /// </summary>
        /// <param name="collectionName">集合名稱。</param>
        /// <param name="documentId">文檔 ID。</param>
        public async Task DeleteData(string collectionName, string documentId)
        {
            DocumentReference docRef = _db.Collection(collectionName).Document(documentId);
            await docRef.DeleteAsync();
        }
    }
}
