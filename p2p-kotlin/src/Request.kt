import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import Node

open class Request {
    fun getRequest(_username : String): String{
        var urlAux = "https://evening-headland-54924.herokuapp.com/username?username=${_username}"
        val url = URL(urlAux)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        con.requestMethod = "GET"

        var status = con.responseCode;
        if(status > 299) {
            print("DEBUG: Changing to secondary server!...\n");
            val urlAuxSecondary = "https://fast-atoll-78029.herokuapp.com/username?username=${_username}"
            val urlSecondary = URL(urlAuxSecondary);
            val conSecondary: HttpURLConnection = urlSecondary.openConnection() as HttpURLConnection
            conSecondary.requestMethod = "GET"

            status = conSecondary.responseCode;

            if(status > 299){
                print("DEBUG: Both servers failed...")
                throw ServersFailedException("Both servers failed");
            }

            val inputSec = BufferedReader(InputStreamReader(conSecondary.inputStream))
            var inputLine: String?
            val contentSec = StringBuffer()
            while (inputSec.readLine().also { inputLine = it } != null) {
                contentSec.append(inputLine)
            }
            inputSec.close()
            return contentSec.toString();

        }
        val input = BufferedReader(InputStreamReader(con.inputStream))
        var inputLine: String?
        val content = StringBuffer()
        while (input.readLine().also { inputLine = it } != null) {
            content.append(inputLine)
        }
        input.close()
        return content.toString();
    }

    fun postRequest(_username: String, _ip: String, _port: Int): String {
        var urlAux = "https://evening-headland-54924.herokuapp.com/add?username=$_username&ip=${_ip}&port=${_port}"
        val url = URL(urlAux+params)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        con.requestMethod = "POST"

        if(con.responseCode > 299){
            //If the status is the connection is higher than 299, 300-500 there is either a server or client error
            //Which means we will try to connect to the secondary discovery server.
            var urlAuxSecondary = "https://fast-atoll-78029.herokuapp.com/add?username=${_username}&ip=${_ip}&port=${_port}"
            var urlSecondary = URL(urlAuxSecondary);
            val conSecondary: HttpURLConnection = urlSecondary.openConnection() as HttpURLConnection
            conSecondary.requestMethod = "POST"

            val inputSec = BufferedReader(InputStreamReader(conSecondary.inputStream))
            var inputLineSec: String?
            val contentSec = StringBuffer()
            while (inputSec.readLine().also { inputLineSec = it } != null) {
                contentSec.append(inputLineSec)
            }
            inputSec.close()
            return contentSec.toString();
        }

        val input = BufferedReader(InputStreamReader(con.inputStream))
        var inputLine: String?
        val content = StringBuffer()
        while (input.readLine().also { inputLine = it } != null) {
            content.append(inputLine)
        }
        input.close()
        return content.toString();
    }
}