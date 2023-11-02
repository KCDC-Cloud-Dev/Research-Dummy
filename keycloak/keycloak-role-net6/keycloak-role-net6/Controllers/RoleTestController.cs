﻿using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace keycloak_role_net6.Controllers
{
    [ApiController]
    [Route("api/role")]
    public class RoleTestController : ControllerBase
    {
        private readonly ILogger<RoleTestController> _logger;

        public RoleTestController(ILogger<RoleTestController> logger)
        {
            _logger = logger;
        }


        [Authorize(Roles = "admin,write")]
        [HttpGet(nameof(Create))]
        public IActionResult Create()
        {
            return Ok("Create OK");
        }

        [Authorize(Roles = "admin,read")]
        [HttpGet(nameof(Read))]
        public IActionResult Read()
        {
            return Ok("Read OK");
        }

        [Authorize(Roles = "admin,write")]
        [HttpGet(nameof(Edit))]
        public IActionResult Edit()
        {
            return Ok("Edit OK");
        }


    }
}
