using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using WebApplication4.Data;
using WebApplication4.Models;
using System.IdentityModel.Tokens.Jwt;
using Microsoft.IdentityModel.Tokens;
using System.Text;

namespace Messaging_WebApp.Controllers
{
    public class temp
    {
        public String token { get; set; }

    }
    public class Temp
    {
        public String UserID { get; set; }
        public String Name { get; set; }

        public String server { get; set; }

    }
    public class Inv
    {
        public String from { get; set; }
        public String to { get; set; }

        public String server { get; set; }

    }

    public class Temp2
    {
        public String Content { get; set; }

    }

    public class Transf
    {
        public String from { get; set; }
        public String to { get; set; }
        public String content { get; set; }

    }

    public class TempUser
    {
        public string username { get; set; }
        public string? name { get; set; }
        public string password { get; set; }

    }
    public class TempUser2
    {
        public String username { get; set; }
        public String password { get; set; }

    }

    [ApiController]
    [Route("api/[controller]")]
    public class ContactsController : Controller
    {
        private readonly WebApplication4Context _context;
        public static List<User> Users;
        public IConfiguration _configuration;

        public ContactsController(WebApplication4Context context, IConfiguration configuration)
        {
            _context = context;
            Users = _context.User.ToList();
            var contacts = _context.Contact.ToList();
            var messages = _context.Message.ToList();
            foreach (var user in Users)
            {
                user.Contacts = contacts.Where(x => x.UserId == user.Username).ToList();
                foreach (var contact in user.Contacts)
                {
                    contact.Messages = messages.Where(x => x.ContactId == contact.Id).ToList();
                }
            }
            _configuration = configuration;
        }
        [HttpPost]

        [Route("login")]
        public async Task<IActionResult> Login([Bind("username,password")] TempUser2 temp)
        {
            if (ModelState.IsValid)
            {
#pragma warning disable CS8604 // Possible null reference argument.
                var check_user = _context.User.Where(x => x.Username == temp.username && x.Password == temp.password);
                if (check_user.Any())               
                {
                    //Signin(check_user.First());
                    var claims = new[] {
                        new Claim(JwtRegisteredClaimNames.Sub, _configuration["JWTParams:Subject"]),
                        new Claim(JwtRegisteredClaimNames.Sub, Guid.NewGuid().ToString()),
                        new Claim(JwtRegisteredClaimNames.Sub, DateTime.UtcNow.ToString()),
                        new Claim("UserID", temp.username),
                    };
                    var secretKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JWTParams:SecretKey"]));
                    var mac = new SigningCredentials(secretKey, SecurityAlgorithms.HmacSha256);
                    var token = new JwtSecurityToken(
                        _configuration["JWTParams:Issuer"],
                        _configuration["JWTParams:Audience"],
                        claims,
                        expires: DateTime.UtcNow.AddMinutes(30),
                        signingCredentials: mac);
                    var tok = new JwtSecurityTokenHandler().WriteToken(token);
                    temp t = new temp() { token = tok };
                    return Json(tok);

                }
                return Json(null);

            }
            return Json(null);
        }

        [HttpPost]
        [Route("register")]
        public async Task<IActionResult> Register([Bind("username,name,password")] TempUser temp)
        {
            if (ModelState.IsValid)
            {
                var check_user = _context.User.Where(x => x.Username == temp.username);
                if (check_user.Any()) { return Json(null); }
                var Contacts = new List<Contact>();
                User user = new User();
                user.Username = temp.username;
                user.Name = temp.name;
                user.Password = temp.password;
                user.Contacts = Contacts;
                Users.Add(user);
                _context.Add(user);
                await _context.SaveChangesAsync();
                return Json("ok");
            }
            return Json(null);
        }

        public String encode(string authorization)
        {
            var stream = authorization.Remove(0, 7);
            var handler = new JwtSecurityTokenHandler();
            var jsonToken = handler.ReadToken(stream);
            var tokenS = jsonToken as JwtSecurityToken;
            var name = tokenS.Claims.ElementAt(3).Value;
            return name;
        }

        [HttpPost]
        [Route("user2")]
        public async Task<IActionResult> AddUser([Bind("username,name,password")] TempUser temp)
        {
            if (ModelState.IsValid)
            {
                //String name = encode(authorization);
                var user = new User() {
                    Username = temp.username,
                    Name = temp.name,
                    Password = temp.password,
                    Contacts = new List<Contact>()
                };
                _context.Add(user);
                await _context.SaveChangesAsync();
                return Ok(user);
            }
            return BadRequest();
        }


        //[Authorize]
        [HttpGet]
        [Route("user")]
        public async Task<IActionResult> GetUser([FromHeader] string authorization)
        {
            if (!string.IsNullOrEmpty(authorization))
            {
                var stream = authorization.Remove(0, 7);
                var handler = new JwtSecurityTokenHandler();
                var jsonToken = handler.ReadToken(stream);
                var tokenS = jsonToken as JwtSecurityToken;
                var name = tokenS.Claims.ElementAt(3).Value;
                var user = _context.User.Where(x => x.Username == name);
                if (user == null)
                    return NotFound();
                return Json(user.First());
            }
            return NotFound();
        }

        [Authorize]
        [HttpGet]
        public async Task<IActionResult> GetContacts([FromHeader] string authorization)
        {
            if (!string.IsNullOrEmpty(authorization))
            {
                String name = encode(authorization);
                var user = Users.Find(x => x.Username == name);
                if (user == null)
                {
                    return NotFound();
                }
                return Json(user.Contacts);
            }
            return NotFound();
        } 

        [Authorize]
        [HttpPost]
        //[Route("{Username}/contacts")]
        public async Task<IActionResult> AddContact([Bind("UserID,Name,server")] Temp temp, [FromHeader] string authorization)
        {
            if (ModelState.IsValid)
            {
                String name = encode(authorization);
                Contact contact = new Contact()
                {
                    UserId = name,
                    Name = temp.Name,
                    Server = temp.server,
                    Contname = temp.UserID,
                    Last = "null",
                    Lastdate = "null"
                };
                var user = Users.Find(x => x.Username == name);
                user.Contacts.Add(contact);
                _context.Add(contact);
                await _context.SaveChangesAsync();
                return Json("added");
            }
            return Json(null);
        }

        [Authorize]
        [HttpGet]
        [Route("{ContID}")]
        public async Task<IActionResult> GetContact(string ContID, [FromHeader] string authorization)
        {
            if (!string.IsNullOrEmpty(ContID))
            {
                String name = encode(authorization);
                var user = Users.Find(x => x.Username == name);
                var contact = user.Contacts.Find(x => x.Contname == ContID);
                if (contact == null)
                {
                    return NotFound();
                }
                return Json(contact);
            }
            return NotFound();
        }

        [Authorize]
        [HttpPut]
        [Route("{ContID}")]
        public async Task<IActionResult> UpdateContact(string ContID, [Bind("Name,server")] Temp temp, [FromHeader] string authorization)
        {
            if (!string.IsNullOrEmpty(ContID) && !string.IsNullOrEmpty(temp.Name) &&
                !string.IsNullOrEmpty(temp.server))
            {
                String name = encode(authorization);
                var user = Users.Find(x => x.Username == name);
                var contact = user.Contacts.Find(x => x.Contname == ContID);
                if (contact == null)
                {
                    return NotFound();
                }
                contact.Name = temp.Name;
                contact.Server = temp.server;
                _context.Contact.Update(contact);
                await _context.SaveChangesAsync();
                return Ok(contact);
            }
            return NotFound();
        }

        [Authorize]
        [HttpDelete]
        [Route("{ContID}")]
        public async Task<IActionResult> DeleteContact(string ContID, [FromHeader] string authorization)
        {
            if (!string.IsNullOrEmpty(ContID))
            {
                String name = encode(authorization);
                var user = Users.Find(x => x.Username == name);
                var contact = user.Contacts.Find(x => x.Contname == ContID);
                if (contact == null)
                {
                    return NotFound();
                }
                user.Contacts.Remove(contact);
                _context.Contact.Remove(contact);
                await _context.SaveChangesAsync();
                return Ok(contact);
            }
            return NotFound();
        }


        [Authorize]
        [HttpGet]
        [Route("{ContID}/messages")]
        public async Task<IActionResult> GetMessages(string ContID, [FromHeader] string authorization)
        {
            if (!string.IsNullOrEmpty(ContID))
            {
                String name = encode(authorization);
                var user = Users.Find(x => x.Username == name);
                var contact = user.Contacts.Find(x => x.Contname == ContID);
                if (contact == null)
                {
                    return NotFound();
                }
                return Json(contact.Messages);
            }
            return NotFound();
        }

        [Authorize]
        [HttpPost]
        [Route("{ContID}/messages")]
        public async Task<IActionResult> AddMessage(String ContID, [Bind("Content")] Temp2 temp, [FromHeader] string authorization)
        {
            if (!string.IsNullOrEmpty(temp.Content))
            {
                String name = encode(authorization);
                var user = Users.Find(x => x.Username == name);
                var contact = user.Contacts.Find(x => x.Contname == ContID);
                if (contact == null)
                {
                    return NotFound();
                }
                Message mess = new Message()
                {
                    Content = temp.Content,
                    Sent = true,
                    Created = DateTime.Now.ToString("dddd, dd MMMM yyyy HH:mm:ss")
                };
                contact.Messages.Add(mess);
                _context.Add(mess);
                _context.Update(contact);
                await _context.SaveChangesAsync();
                return Json("added");
            }
            return NotFound();
        }

        [Authorize]
        [HttpGet]
        [Route("{ContID}/messages/{msgID}")]
        public async Task<IActionResult> GetMessage(string ContID, int msgID, [FromHeader] string authorization)
        {
            if (!string.IsNullOrEmpty(ContID))
            {
                String name = encode(authorization);
                var user = Users.Find(x => x.Username == name);
                var contact = user.Contacts.Find(x => x.Contname == ContID);
                if (contact == null)
                {
                    return NotFound();
                }
                var mess = contact.Messages.Find(x => x.Id == msgID);
                if (mess == null)
                {
                    return NotFound();
                }
                return Json(mess);
            }
            return NotFound();
        }

        [Authorize]
        [HttpPut]
        [Route("{ContID}/messages/{msgID}")]
        public async Task<IActionResult> EditMessage(string ContID, int msgID, [Bind("Content")] Temp2 temp, [FromHeader] string authorization)
        {
            if (!string.IsNullOrEmpty(ContID) && ModelState.IsValid)
            {
                String name = encode(authorization);
                var user = Users.Find(x => x.Username == name);
                var contact = user.Contacts.Find(x => x.Contname == ContID);
                if (contact == null)
                {
                    return NotFound();
                }
                var mess = contact.Messages.Find(x => x.Id == msgID);
                if (mess == null)
                {
                    return NotFound();
                }
                mess.Content = temp.Content;
                _context.Update(mess);
                await _context.SaveChangesAsync();
                return Ok(mess);
            }
            return NotFound();
        }

        [Authorize]
        [HttpDelete]
        [Route("{ContID}/messages/{msgID}")]
        public async Task<IActionResult> DeleteMessage(string ContID, int msgID, [Bind("Content")] Temp2 temp, [FromHeader] string authorization)
        {
            if (!string.IsNullOrEmpty(ContID) && ModelState.IsValid)
            {
                String name = encode(authorization);
                var user = Users.Find(x => x.Username == name);
                var contact = user.Contacts.Find(x => x.Contname == ContID);
                if (contact == null)
                {
                    return NotFound();
                }
                var mess = contact.Messages.Find(x => x.Id == msgID);
                if (mess == null)
                {
                    return NotFound();
                }
                contact.Messages.Remove(mess);
                _context.Remove(mess);
                await _context.SaveChangesAsync();
                return Ok(contact);
            }
            return NotFound();
        }

        [HttpPost]
        [Route("invitations")]
        public async Task<IActionResult> Invite([Bind("from,to,server")] Inv invite)
        {
            if (ModelState.IsValid)
            {
                var user = Users.Find(x => x.Username == invite.to);
                if (user == null) { return NotFound(); }
                Contact contact = new Contact() { Contname = invite.from, UserId = invite.to, Name = invite.from, Server = invite.server, Last = "null", Lastdate = "null" };
                user.Contacts.Add(contact);
                _context.Add(contact);
                await _context.SaveChangesAsync();
                return Ok(contact);
            }
            return BadRequest();
        }

        [HttpPost]
        [Route("transfer")]
        public async Task<IActionResult> Transfer([Bind("from,to,content")] Transf message)
        {
            if (ModelState.IsValid)
            {
                var user = Users.Find(x => x.Username == message.to);
                if (user == null) { return NotFound(); }
                var cont = user.Contacts.Find(x => x.Contname == message.from);
                if (cont == null) { return NotFound(); }
                Message msg = new Message() { Content = message.content, Sent = false, Created = DateTime.Now.ToString("dddd, dd MMMM yyyy HH:mm:ss") };
                cont.Messages.Add(msg);
                _context.Add(msg);
                _context.Update(cont);
                await _context.SaveChangesAsync();
                return Json("nice");
            }
            return Json("fuck");
        }
    }
}
