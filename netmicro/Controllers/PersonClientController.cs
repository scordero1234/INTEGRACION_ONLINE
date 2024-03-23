
using Microsoft.AspNetCore.Mvc;
 
namespace netmicro.Controllers;
/**
    * A controller la persona cliente
    @Author: Santiago Cordero
    */ 
[ApiController]
[Route("[controller]")]
 public class PersonClientController : ControllerBase
 {
    static List<PersonClient> personClients = new List<PersonClient>()
    {
        new PersonClient(1, "0301645354", "Santiago Cordero ", 35, "", 2, 100)
    };
 
    private readonly ILogger<PersonClientController> _logger;
 
    public PersonClientController(ILogger<PersonClientController> logger)
    {
        _logger = logger;
    }
 
    [HttpGet(Name = "GetClients")]
    public IEnumerable<PersonClient> Get(){
        return personClients.ToArray();
    }
 
    [HttpPost(Name = "CreateClient")]
    public string Post(PersonClient personClient){
        personClients.Add(personClient);
        return "OK";
    } 

 }