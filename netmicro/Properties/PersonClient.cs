/** 
@Author: Santiago Cordero

*/
public class PersonClient
{   public PersonClient(int Codigo, string Identificacion, string Name, int Age, string Email, string ClientType, decimal Amount)
    {
        this.Codigo = Codigo;
        this.Identificacion = Identificacion;
        this.Name = Name;
        this.Age = Age;
        this.Email = Email;
        this.ClientType = ClientType;
        this.Amount = Amount;
    }
      public int Codigo { get; set; }
    public string Identificacion { get; set; }
    public string Name { get; set; }
    public int Age { get; set; }
    public string Email { get; set; }
    public string ClientType { get; set; }
    public decimal Amount { get; set; }
}