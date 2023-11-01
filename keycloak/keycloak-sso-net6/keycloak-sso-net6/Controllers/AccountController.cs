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
    public class AccountController : ControllerBase
    {

        private readonly ILogger<AccountController> _logger;

        public AccountController(ILogger<AccountController> logger)
        {
            _logger = logger;
        }

        //[Authorize(Roles = "write_api")]
        [Authorize]
        [HttpGet(nameof(Login))]
        public async Task<string> Login()
        {
            return "auth check ok";
        }

        [Authorize(Policy = "MustHaveGetRole")]
        //[Authorize]
        [HttpGet("GetRoles")]
        public async Task<IActionResult> GetRolesAsync()
        {
            string accessToken = await HttpContext.GetTokenAsync("access_token");
            string idToken = await HttpContext.GetTokenAsync("id_token");
            string refreshToken = await HttpContext.GetTokenAsync("refresh_token");

            var claims = User.Claims.ToList();

            var roles = claims.Where(x => x.Type == "realm_access.roles")
               .Select(x => x.Value).ToList();

            return Ok(new
            {
                Roles = accessToken
            });
        }

        [HttpGet("trigger-auth")]
        public IActionResult TriggerAuth()
        {
            return Challenge(new AuthenticationProperties { RedirectUri = "/" }, OpenIdConnectDefaults.AuthenticationScheme);
        }

    }
}
