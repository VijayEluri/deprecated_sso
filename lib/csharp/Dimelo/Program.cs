using System;

namespace Dimelo
{
    class Program
    {
        static void Main(string[] args)
        {

            var server = new Uri("https://example.users.feedback20.com/");
            var service = new Uri("http://example.ideas.feedback20.com/");
            var salt = "bfc9396b7c710746b19a1297e70d1716";

            //it should works with only required params
            var sso = new SSO(server, salt, service);
            sso.FirstName = "Renaud";
            sso.Uuid = "42";
            sso.Expires = 1277309412;
            
            if (sso.BuildUri().ToString() != "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&expires=1277309412&firstname=Renaud&service=http://example.ideas.feedback20.com/&token=c34d2ee42be49635c027d1a7c39fd2f5d8411f39&uuid=42")
            {
                Console.Write("F");
            }
            else {
                Console.Write(".");
            }

            sso.LastName = "Morane";
            sso.AvatarUrl = new Uri("http://example.com/avatar.png");
            sso.Email = "foo@example.com";
            sso.Charset = "utf-8";
            sso.Role = "user";

            // it should works with all params do
            if (sso.BuildUri().ToString() != "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&avatar_url=http://example.com/avatar.png&charset=utf-8&email=foo@example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http://example.ideas.feedback20.com/&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42")
            {
                Console.Write("F");
            }
            else
            {
                Console.Write(".");
            }

            Console.ReadLine();
        }
    }
}
