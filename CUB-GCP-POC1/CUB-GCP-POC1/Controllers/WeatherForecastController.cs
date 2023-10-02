using System;
using Google.Apis.Auth.OAuth2;
using System.Threading.Channels;
using Google.Cloud.Firestore;
using Microsoft.AspNetCore.Mvc;
using Google.Cloud.Firestore.V1;
using Grpc.Core;
using System.Net;
using static Google.Cloud.Firestore.V1.StructuredQuery.Types;

namespace CUB_GCP_POC1.Controllers;

[ApiController]
[Route("[controller]")]
public class WeatherForecastController : ControllerBase
{
    private static readonly string[] Summaries = new[]
    {
        "Freezing", "Cool", "Warm", "Hot"
    };

    private readonly ILogger<WeatherForecastController> _logger;
    private readonly string ProjectID = "projecta-400301";

    public WeatherForecastController(ILogger<WeatherForecastController> logger)
    {
        _logger = logger;
        //InitializeProjectId("POC1");

    }


    [HttpPost("PostWeatherForecastWithoutAuth")]
    public async Task<IActionResult> PostWeatherForecastWithoutAuth()
    {
        //GoogleCredential cred = GoogleCredential.FromFile("projecta-400301-71950c887569.json");

        FirestoreDb db = new FirestoreDbBuilder { ProjectId = ProjectID}.Build();

        //add data 1

        await AddData1(db);

        //add data 2
        await AddData2(db);

        return Ok();
    }
    [HttpPost("PostWeatherForecastWithAuth")]
    public async Task<IActionResult> PostWeatherForecastWithAuth()
    {
        GoogleCredential cred = GoogleCredential.FromFile("projecta-400301-71950c887569.json");

        FirestoreDb db = new FirestoreDbBuilder { ProjectId = ProjectID, Credential = cred }.Build();

        //add data 1

        await AddData1(db);

        //add data 2
        await AddData2(db);

        return Ok();
    }


    [HttpGet("GetWeatherForecastWithAuth")]
    public async Task<ActionResult<IEnumerable<WeatherForecast>>> GetWeatherForecastWithAuth()
    {
        GoogleCredential cred = GoogleCredential.FromFile("projecta-400301-71950c887569.json");

        FirestoreDb db = new FirestoreDbBuilder { ProjectId = ProjectID, Credential = cred }.Build();

        CollectionReference usersRef = db.Collection("weather");

        //
        QuerySnapshot snapshot = await usersRef.GetSnapshotAsync();

        List<WeatherForecast> weatherForecasts = new List<WeatherForecast>();

        foreach (DocumentSnapshot document in snapshot.Documents)
        {
            Console.WriteLine("User: {0}", document.Id);
            Dictionary<string, object> documentDictionary = document.ToDictionary();

            try
            {

                weatherForecasts.Add(new WeatherForecast
                {
                    Date = DateTime.Parse(documentDictionary["Date"].ToString() ?? ""),
                    Summary = (String)documentDictionary["Summary"],
                    TemperatureC = int.Parse((documentDictionary["TemperatureC"].ToString()??"0")),
                });

            }catch(Exception ex)
            {
                _logger.LogError(ex.StackTrace);
                return BadRequest(ex);

            }
        }



        return weatherForecasts
        .ToArray();
    }

    [HttpGet("GetWeatherForecastWithoutAuth")]
    public async Task<ActionResult<IEnumerable<WeatherForecast>>> GetWeatherForecastWithoutAuth()
    {
        try
        {

            FirestoreDb db = new FirestoreDbBuilder { ProjectId = ProjectID }.Build();

            CollectionReference usersRef = db.Collection("weather");

            //
            QuerySnapshot snapshot = await usersRef.GetSnapshotAsync();

            List<WeatherForecast> weatherForecasts = new List<WeatherForecast>();

            foreach (DocumentSnapshot document in snapshot.Documents)
            {
                Console.WriteLine("User: {0}", document.Id);
                Dictionary<string, object> documentDictionary = document.ToDictionary();

                weatherForecasts.Add(new WeatherForecast
                {
                    Date = DateTime.Parse(documentDictionary["Date"].ToString() ?? ""),
                    Summary = (String)documentDictionary["Summary"],
                    TemperatureC = int.Parse((documentDictionary["TemperatureC"].ToString() ?? "0")),
                });

            }



            return weatherForecasts
            .ToArray();
        }

        catch (Exception ex)
        {
            _logger.LogError(ex.StackTrace);
            return BadRequest(ex);
        }
    }

    private async Task AddData1(FirestoreDb db)
    {
        DocumentReference docRef = db.Collection("weather").Document("W1");
        Dictionary<string, object> weather = new Dictionary<string, object>
            {
                { "Date", DateTime.Now.ToShortDateString() },
                { "TemperatureC", Random.Shared.Next(-20, 55) },
                { "Summary", Summaries[Random.Shared.Next(Summaries.Length)] },
            };
        await docRef.SetAsync(weather);
        _logger.LogInformation("Added data to the alovelace document in the users collection.");


    }

    private async Task AddData2(FirestoreDb db)
    {
        DocumentReference docRef = db.Collection("weather").Document("W2");


        Dictionary<string, object> weather = new Dictionary<string, object>
        {
                { "Date", DateTime.Now.ToShortDateString() },
                { "TemperatureC", Random.Shared.Next(-20, 55) },
                { "Summary", Summaries[Random.Shared.Next(Summaries.Length)] },
        };


        await docRef.SetAsync(weather);
        _logger.LogInformation("Added data to the aturing document in the users collection.");


    }
}

