#See https://aka.ms/containerfastmode to understand how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/aspnet:6.0 AS base
WORKDIR /app



FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
WORKDIR /src
COPY ["CUB-GCP-POC1/CUB-GCP-POC1.csproj", "CUB-GCP-POC1/"]
RUN dotnet restore "CUB-GCP-POC1/CUB-GCP-POC1.csproj"
COPY . .
WORKDIR "/src/CUB-GCP-POC1"
RUN dotnet build "CUB-GCP-POC1.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "CUB-GCP-POC1.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENV ASPNETCORE_URLS=http://*:8080
EXPOSE 8080
ENTRYPOINT ["dotnet", "CUB-GCP-POC1.dll"]
