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


        [HttpGet(Name = nameof(Login))]
        public string Login()
        {
            return "auth check ok";
        }

        [HttpGet(Name = nameof(GetAccount))]
        public string GetAccount()
        {
            return "Mario";
        }
    }
}
