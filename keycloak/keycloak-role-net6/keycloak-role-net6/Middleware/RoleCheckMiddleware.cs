namespace keycloak_role_net6.Middleware
{
    public class RoleCheckMiddleware
    {
        private readonly RequestDelegate _next;

        public RoleCheckMiddleware(RequestDelegate next)
        {
            _next = next;
        }

        public async Task Invoke(HttpContext context)
        {
            // 跳過登入路徑的角色檢查
            if (context.Request.Path.StartsWithSegments("/api/user/Login") || context.Request.Path.StartsWithSegments("/api/user/Logout"))
            {
                await _next(context);
                return;
            }

            // 檢查是否有相關角色的邏輯
            if (context.User.Identity.IsAuthenticated && !context.User.IsInRole("所需角色"))
            {
                // 如果用戶角色不符合，返回自定義錯誤
                context.Response.StatusCode = 403; // Forbidden
                await context.Response.WriteAsync("Role not access");
                return;
            }

            await _next(context);
        }
    }
}
