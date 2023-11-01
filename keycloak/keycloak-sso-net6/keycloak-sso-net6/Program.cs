using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication.OpenIdConnect;
using Microsoft.IdentityModel.Tokens;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

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
    options.LoginPath = "/Account/Login";
})
.AddJwtBearer("Bearer", options =>
{
    // 這部分是為了Access Token
    options.Authority = "http://localhost:8081/auth/realms/api-role-lab";
    
    options.Audience = "admin-rest-client";
    options.RequireHttpsMetadata = false;

    options.TokenValidationParameters = new TokenValidationParameters
    {
        ValidateIssuer = true,
        ValidateAudience = false,
        ValidateLifetime = true,
        ValidateIssuerSigningKey = true,
        ValidIssuer = "http://localhost:8081/auth/realms/api-role-lab"
    };
}).AddOpenIdConnect(options =>
{
    options.SignInScheme = CookieAuthenticationDefaults.AuthenticationScheme;
    options.RequireHttpsMetadata = false;
    options.Authority = "http://localhost:8081/auth/realms/api-role-lab";
    options.ClientId = "admin-rest-client";
    options.ClientSecret = "6GXJRf9rcrg0ccgi4AAmPk42ORUYN9iS";
    options.ResponseType = "code";
    options.SaveTokens = true;
    options.Scope.Add("openid");
    options.Scope.Add("profile");
    options.Scope.Add("roles");

    options.TokenValidationParameters = new TokenValidationParameters
    {
        NameClaimType = "preferred_username",
        RoleClaimType = "roles",
        ValidateIssuer = true,
        ValidIssuer = "http://localhost:8081/auth/realms/api-role-lab"
    };


    options.Events = new OpenIdConnectEvents
    {
        OnTokenResponseReceived = context =>
        {
            // 檢查這裡是否有拿到 Access Token
            var accessToken = context.TokenEndpointResponse.AccessToken;
            Console.WriteLine($"*******************{accessToken}");
            return Task.CompletedTask;
        },
    };

});



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
