using System.IdentityModel.Tokens.Jwt;

namespace keycloak_sso_net6.Middleware
{
    public class JwtMiddleware
    {
        private readonly RequestDelegate _next;

        public JwtMiddleware(RequestDelegate next)
        {
            _next = next;
        }

        public async Task InvokeAsync(HttpContext context)
        {
            /*
            foreach (var header in context.Request.Headers)
            {
                Console.WriteLine($"{header.Key}: {header.Value}");
            }
            */

            string authorizationHeader = context.Request.Headers["Authorization"];

            if (authorizationHeader != null && authorizationHeader.StartsWith("Bearer "))
            {
                string jwtToken = authorizationHeader.Substring("Bearer ".Length).Trim();

                // 解析 JWT 來獲取角色信息。這裡為了簡單起見，我們假設角色信息存在於 "roles" 欄位。
                var handler = new JwtSecurityTokenHandler();
                var jsonToken = handler.ReadToken(jwtToken);
                var tokenS = jsonToken as JwtSecurityToken;

                var roles = tokenS.Claims.First(claim => claim.Type == "roles").Value;

                // 在這裡，你可以印出或儲存角色信息。
                Console.WriteLine($"Roles: {roles}");
            }

            await _next(context);
        }
    }
}
