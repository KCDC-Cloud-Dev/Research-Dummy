using CUB_GCP_POC1.Service;



var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddScoped(option => { 

    var projectID = builder.Configuration["ProjectID"];
    var serviceAccountAuth = builder.Configuration["ServiceAccountAuth"];
    return new FileStoreService(projectID, serviceAccountAuth);
});

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();


var app = builder.Build();

// Configure the HTTP request pipeline.
// For Demo
//if (app.Environment.IsDevelopment())
//{
    app.UseSwagger();
    app.UseSwaggerUI();
//}

//app.UseHttpsRedirection();

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




