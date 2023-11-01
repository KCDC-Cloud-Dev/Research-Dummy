using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authentication.OpenIdConnect;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;

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

        [Authorize]
        [HttpGet(nameof(Login))]
        public async Task<string> Login()
        {
            return "auth check ok";
        }

        //[Authorize]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        [HttpGet(nameof(GetRoles))]
        public IActionResult GetRoles()
        {
            // 尋找所有的 "role" claims
            var roles = User.Claims.Where(c => c.Type == ClaimTypes.Role).Select(c => c.Value).ToList();
            return Ok(new { Roles = roles });
        }

        [HttpGet("trigger-auth")]
        public IActionResult TriggerAuth()
        {
            return Challenge(new AuthenticationProperties { RedirectUri = "/" }, OpenIdConnectDefaults.AuthenticationScheme);
        }

    }
}
