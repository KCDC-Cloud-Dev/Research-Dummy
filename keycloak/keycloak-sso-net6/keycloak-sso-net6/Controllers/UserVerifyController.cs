using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace keycloak_sso_net6.Controllers
{
 
    [ApiController]
    [Route("api/user")]
    public class UserVerifyController : ControllerBase
    {

        private readonly ILogger<UserVerifyController> _logger;

        public UserVerifyController(ILogger<UserVerifyController> logger)
        {
            _logger = logger;
        }

        //[Authorize(Roles = "api-getaccount")]
        [Authorize]
        [HttpGet(nameof(Login))]
        public string Login()
        {

            var user = GetUserName();
            return "auth check ok";
        
        }

        [Authorize]
        [HttpGet(nameof(GetAccount))]
        public string GetAccount()
        {
            return "Mario";
        }

        private string GetUserName()
        {
            //return "Mario";
            var userNameClaim = User.Claims.FirstOrDefault(c => c.Type == "nameid");
            if (userNameClaim == null)
            {
                throw new Exception("User claim not found.");
            }
            return userNameClaim.Value;
        }
    }
}
