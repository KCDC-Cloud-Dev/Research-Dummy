namespace CUB_GCP_POC1.Models.Dto
{
    public class User
    {

        public string Name { get; set; }
        public int Heihgt { get; set; }

        public float Weight { get; set; }
        public int Age { get; set; }

        public User(string name)
        {
            Name = name;
        }
    }
}
