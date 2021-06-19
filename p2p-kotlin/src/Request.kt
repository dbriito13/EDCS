import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

open class Request {
    fun getRequest(_username : String): String{
        var urlAux = "https://evening-headland-54924.herokuapp.com/username?username=${_username}"
        val url = URL(urlAux)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        con.requestMethod = "GET"

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
        val urlAux = "https://evening-headland-54924.herokuapp.com/add?"
        val params = "username=$_username&ip=${_ip}&port=${_port}"
        val url = URL(urlAux+params)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        con.requestMethod = "POST"

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