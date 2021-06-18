class NodeAddress (_host: String, _port : Int){
    val host = _host;
    val port = _port;

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NodeAddress

        if (host != other.host) return false
        if (port != other.port) return false

        return true
    }
}