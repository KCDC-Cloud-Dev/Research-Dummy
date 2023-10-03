using CUB_GCP_POC1.Models;
using CUB_GCP_POC1.Models.Dto;
using CUB_GCP_POC1.Service;
using Microsoft.AspNetCore.Mvc;

namespace CUB_GCP_POC1.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class FirestoreUserTestController : ControllerBase
    {
        private readonly FileStoreService _fileStoreService;

        public FirestoreUserTestController(FileStoreService fileStoreService)
        {

            _fileStoreService = fileStoreService;
        }

        [HttpPost(nameof(CreateUser))]
        public async Task<ActionResult<ApiResponse<object>>> CreateUser(User user)
        {

            var userInfo = new Dictionary<string, object>
            {
                { "age", user.Age },
                { "height", user.Heihgt},
                { "weight", user.Weight}
            };

            await _fileStoreService.CreateData(collectionName:"User", documentId:user.Name, data: userInfo);

            return Ok(new ApiResponse<object>
            {
                Message = "Create user sucessful",
            });
        }

        [HttpGet(nameof(GetUser))]
        public async Task<ActionResult<ApiResponse<Dictionary<string, object>>>> GetUser(string name)
        {
            var users = await _fileStoreService.ReadData(collectionName: "User", documentId: name);

            return Ok(new ApiResponse<Dictionary<string, object>>
            {
                Data = users,
                Message = "Get user sucessful",
            });;
        }

        [HttpPost(nameof(ModifyUser))]
        public async Task<ActionResult<ApiResponse<object>>> ModifyUser(User user)
        {

            var userInfo = new Dictionary<string, object>
            {
                { "age", user.Age },
                { "height", user.Heihgt},
                { "weight", user.Weight}
            };

            await _fileStoreService.UpdateData(collectionName: "User", documentId: user.Name, data: userInfo);

            return Ok(new ApiResponse<object>
            {
                Message = "Create user sucessful",
            });
        }


        [HttpDelete(nameof(DeleteUser))]
        public async Task<ActionResult<ApiResponse<object>>> DeleteUser(string name)
        {
            await _fileStoreService.DeleteData(collectionName: "User", documentId: name);

            return Ok(new ApiResponse<object>
            {
                Message = "Delete user successful",
            });
        }
    }
}
