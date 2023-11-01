using Microsoft.AspNetCore.Authorization;
using Newtonsoft.Json;
using System.Linq;

namespace keycloak_sso_net6.Middleware
{
    public class MustHaveGetRoleRequirement : IAuthorizationRequirement
    {
        public List<string> RequiredRoles { get; }

        public MustHaveGetRoleRequirement(List<string> requiredRoles = null)
        {
            RequiredRoles = requiredRoles ?? new List<string>();
        }
    }
    public class MustHaveGetRoleHandler : AuthorizationHandler<MustHaveGetRoleRequirement>
    {
        protected override Task HandleRequirementAsync(AuthorizationHandlerContext context, MustHaveGetRoleRequirement requirement)
        {
            var resourceAccessClaim = context.User.Claims.FirstOrDefault(c => c.Type == "resource_access");
            if (resourceAccessClaim != null)
            {
                var resourceAccess = JsonConvert.DeserializeObject<dynamic>(resourceAccessClaim.Value);
               
                var userRoles = resourceAccess?["admin-rest-client"]?["roles"]?.ToObject<List<string>>();

                if(userRoles==null)
                    return Task.CompletedTask;

                bool roleFound = false;
                foreach (var role in userRoles)
                {
                    if (requirement.RequiredRoles.Contains(role))
                    {
                        roleFound = true;
                        break;
                    }
                }

                if (roleFound)
                {
                    context.Succeed(requirement);
                }

                /*
                if (userRoles != null && userRoles.Intersect(requirement.RequiredRoles).Any())
                {
                    context.Succeed(requirement);
                }
                */
                return Task.CompletedTask;

                /*
                if (userRoles != null)
                {
                    if (userRoles.Intersect(requirement.RequiredRoles).Any())
                    {
                        context.Succeed(requirement);
                    }
                }
                */
            }
            return Task.CompletedTask;
        }
    }
}
