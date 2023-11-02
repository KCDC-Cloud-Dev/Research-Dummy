using Microsoft.AspNetCore.Authorization;

namespace keycloak_role_net6.Middleware
{
    public class GetKeycloakRoleRequirement : IAuthorizationRequirement
    {
        public List<string> RequiredRoles { get; }

        public GetKeycloakRoleRequirement(List<string> requiredRoles = null)
        {
            RequiredRoles = requiredRoles ?? new List<string>();
        }
    }
}
