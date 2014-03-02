
import org.itsnat.core.http {ItsNatHttpServlet}

"The classic Hello World program"
shared void hello(String name = "World") {
    print("Hello, `` name ``!");
}

"The runnable method of the module." 
shared void run(){
    if (nonempty args=process.arguments) {
        for (arg in args) {
            hello(arg);
        }
    }
    else {
        hello();
    }
}


"ItsNat Example"
shared void ceylonex_init(ItsNatHttpServlet itsNatServlet,String pathPrefix) {
    print("Hello, `` pathPrefix ``!");
}

