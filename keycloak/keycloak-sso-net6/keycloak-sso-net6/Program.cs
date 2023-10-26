using keycloak_sso_net6.Middleware;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authentication.OpenIdConnect;
using Microsoft.IdentityModel.Tokens;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

/*
builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
       .AddJwtBearer(options =>
       {
           options.Authority = "http://localhost:8082/realms/api-role-auth";
           options.TokenValidationParameters = new TokenValidationParameters
           {
               ValidateIssuer = true,
               ValidateAudience = false,
               ValidateLifetime = true,
               ValidateIssuerSigningKey = true,
               ValidIssuer = "http://localhost:8082/realms/api-role-auth",
               ValidAudience = "web-service"
           };
       });
*/

builder.Services.AddAuthentication(options =>
{
    options.DefaultScheme = CookieAuthenticationDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = OpenIdConnectDefaults.AuthenticationScheme;
}) 
.AddCookie(options =>
{
    options.LoginPath = "/Account/Login";
})
.AddOpenIdConnect(options =>
{
    options.RequireHttpsMetadata = false;
    options.Authority = "http://localhost:8082/realms/api-role-auth";
    options.ClientId = "web-service";
    options.ClientSecret = "e96trjbKczrN7iew9jRDDZUWVEfeG6A4";
    options.ResponseType = "code";
    options.Scope.Add("openid");
    options.Scope.Add("profile");

    /*
    options.TokenValidationParameters = new TokenValidationParameters
    {
        NameClaimType = "preferred_username",
        RoleClaimType = "roles",
        ValidateIssuer = true,
        ValidIssuer = "http://localhost:8082/realms/api-role-auth"
    };
    */
});



var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
//app.UseRouting();


app.UseAuthentication();
app.UseAuthorization();
app.UseMiddleware<JwtMiddleware>();

app.MapControllers();

app.Run();
