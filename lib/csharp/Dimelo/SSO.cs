using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Security.Cryptography;
using System.Text;
using System.Web;
namespace Dimelo
{
    class SSO
    {

        private static string[] TOKENIZED_PARAMS = {"avatar_url", "email", "expires", "firstname", "lastname", "role", "uuid"};
        private static string[] PARAMS = {"avatar_url", "charset", "email", "expires", "firstname", "lastname", "role", "service", "token", "uuid" };

        private string salt;
        private string server;
        
        private IDictionary<String, String> Parameters;
        public NameValueCollection URLEncodedParameters { get {
            var query =  HttpUtility.ParseQueryString(string.Empty);
            foreach (string key in PARAMS) { 
                if (Parameters.ContainsKey(key)) {
                    query[key] = Parameters[key];
                }
            }
            return query;
        } }

        public long Timestamp { get { return (DateTime.UtcNow.Ticks - DateTime.Parse("01/01/1970 00:00:00").Ticks) / 10000000; } }
        public Uri AvatarUrl { set { Parameters["avatar_url"] = value.ToString(); } }
        public string Charset { set { Parameters["charset"] = value; } }
        public string Email { set { Parameters["email"] = value; } }
        public long ExpiresIn { set { Expires = value + Timestamp; } }
        public long Expires { set { Parameters["expires"] = value.ToString(); } }
        public string FirstName { set { Parameters["firstname"] = value; } }
        public string LastName { set { Parameters["lastname"] = value; } }
        public string Role { set { Parameters["role"] = value; } }
        public string Uuid { set { Parameters["uuid"] = value; } }

        public SSO(Uri server, String salt, Uri Service)
        {
            Parameters = new Dictionary<String, String>();
            this.server = server.ToString();
            this.salt = salt;
            Parameters["service"] = Service.ToString();
        }

        public Uri BuildUri() 
        {
            Parameters["token"] = BuildToken();
            return new Uri(server.ToString() + "cas/login?auth=sso&type=acceptor&" + URLEncodedParameters.ToString());
        }

        public string BuildToken() {
            return SHA1(TwoWayJoin(":", "-", Parameters, TOKENIZED_PARAMS) + salt);
        }

        private string TwoWayJoin(string ItemSep, string PairSep, IDictionary<string, string> Hash, string[] Keys)
        {
            List<string> items = new List<string>();
            foreach (string key in Keys) {
                if (Parameters.ContainsKey(key)) {
                    items.Add(key + PairSep + Parameters[key]);
                }
            }
            return String.Join(ItemSep, items.ToArray());           
        }

        public string SHA1(string source)
        {
            var UE = new ASCIIEncoding();
            byte[] HashValue, MessageBytes = UE.GetBytes(source);
            SHA1Managed SHhash = new SHA1Managed();
            string strHex = "";
            HashValue = SHhash.ComputeHash(MessageBytes);
            foreach(byte b in HashValue) {
                    strHex += String.Format("{0:x2}", b);
            }
            return strHex;
        }
    }
}
