using CUB_GCP_POC1.Service;



var builder = WebApplication.CreateBuilder(args);



if (builder.Environment.IsDevelopment())
{
    builder.Services.AddScoped(option => {

        var projectID = builder.Configuration["ProjectID"];
        var serviceAccountAuth = builder.Configuration["ServiceAccountAuth"];
        return new FileStoreService(projectID, serviceAccountAuth);
    });
}
else
{
    // Cloud Run環境變數
    builder.Services.AddScoped(option => {

        var projectID = Environment.GetEnvironmentVariable("ProjectID");
        var serviceAccountAuth = Environment.GetEnvironmentVariable("ServiceAccountAuth");
        return new FileStoreService(projectID, serviceAccountAuth);
    });
}
    // Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();


var app = builder.Build();
app.UseSwagger();
app.UseSwaggerUI();
app.UseAuthorization();

app.MapControllers();

if (app.Environment.IsDevelopment())
{
    app.Run();

}
else
{
    //For GCP Use
    var port = Environment.GetEnvironmentVariable("PORT") ?? "8080";
    var url = $"http://0.0.0.0:{port}";
    app.Run(url);

}




