using keycloak_sso_net6.Middleware;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication.OpenIdConnect;
using Microsoft.AspNetCore.Authorization;
using Microsoft.IdentityModel.Protocols.OpenIdConnect;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Net;
using System.Security.Claims;
using System.Text.Json;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddSingleton<IAuthorizationHandler, MustHaveGetRoleHandler>();
builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddAuthentication(options =>
{
    options.DefaultScheme = CookieAuthenticationDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = OpenIdConnectDefaults.AuthenticationScheme;
})
.AddCookie(options =>
{
    options.LoginPath = "/api/user/Login";
    options.Cookie.Name = "keycloak.cookie";
    options.Cookie.MaxAge = TimeSpan.FromMinutes(60);
    options.Cookie.SecurePolicy = CookieSecurePolicy.SameAsRequest;
    options.SlidingExpiration = true;
}).AddOpenIdConnect(options =>
{
    options.Authority = "http://localhost:8082/realms/api-role-lab";
    options.RequireHttpsMetadata = false; // 只推薦在開發環境使用
    options.ClientId = "admin-rest-client";
    //options.ClientSecret = "6GXJRf9rcrg0ccgi4AAmPk42ORUYN9iS";
    options.ClientSecret = "obxz7kEQ1IUMFMmJynouwwxcK6UgMsR0";
    //options.ResponseType = "code";
    options.ResponseType = OpenIdConnectResponseType.Code;
    // 操操操操 就少這行!!!!!!!
    options.SaveTokens = true;
    
    options.Scope.Add("openid");
    options.Scope.Add("profile");
    options.Scope.Add("email");
    options.Scope.Add("roles");
    options.TokenValidationParameters = new TokenValidationParameters
    {
        NameClaimType = "preferred_username",
        RoleClaimType = ClaimTypes.Role,
        ValidateIssuer = true
    };
});

List<string> requiredRoles = new List<string> { "get" };
builder.Services.AddAuthorization(options =>
{
    options.AddPolicy("MustHaveGetRole", policy =>
    {
        policy.Requirements.Add(new MustHaveGetRoleRequirement(requiredRoles));
    });
});

/*
builder.Services.AddAuthorization(options =>
{
    options.AddPolicy("MustHaveGetRole", policy =>
    {
        policy.Requirements.Add(new MustHaveGetRoleRequirement());
    });
});
*/


var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.UseAuthentication();
app.UseAuthorization();
app.MapControllers();

app.Run();
