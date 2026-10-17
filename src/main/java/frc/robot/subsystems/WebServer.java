package frc.robot.subsystems;

import java.io.*;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.generated.TunerConstants;
import edu.wpi.first.units.Units;


public class WebServer extends SubsystemBase {
    
    private static CommandSwerveDrivetrain drivetrain;
    private static Shooter shooter;

    //The data that is updated periodically by the webserver
    private static final Map<String, Double> DATALIST = Map.of(
        "ShooterKP",shooter.pids.Slot0.kP,
        "ShooterKI",shooter.pids.Slot0.kI,
        "ShooterKD",shooter.pids.Slot0.kD,
        "ShooterSpeed",shooter.getVelocityRps()
    );
    private static IHTTPSession session;
    private final DashboardServer server;    


    public WebServer(CommandSwerveDrivetrain drivetrain, Shooter shooter) {
        WebServer.drivetrain = drivetrain;
        WebServer.shooter = shooter;
        this.server=new DashboardServer(Constants.WebServer.portNumber);
        try {
            server.start(5000, false);
            System.out.println("Dashboard: http://" + getLocalIp() + ":"+Constants.WebServer.portNumber+"/");
                
            } catch (IOException e) {
                stopServer();
            }
        }

        
    public void stopServer() {
            if (server != null) server.stop();
        }
    
    private String getLocalIp() {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {
                return "127.0.0.1";
            }
        }
    
        private static class DashboardServer extends NanoHTTPD {
    
            public DashboardServer(int port) {
                super(port);
            }
            //A map containing a uri token and the response value to serve. whatever is in this map will execute when a uri is referenced
            //Every method returns a HTTP response to confirm, but other things may be executed in the method
            Map<String,Response> uris= Map.of(
            "/data",getData(), //gets the main json datastream from the robot
            "/setpid",setPIDs(), //sets the pids via a uri request
            "/setmotor",setMotor() //will run the motor for a specified time
            );

            //both these methods will automatically get a speified value from the uri token
            private double getDouble(Map<String, List<String>> p,String id,int index){
                try {
                    return Double.parseDouble(p.get(id).get(index));
                }
                catch (Exception e){
                    return 0;
                }
            }
            private double getDouble(Map<String, List<String>> p,String id){
                
                try {
                    return Double.parseDouble(p.get(id).get(0));
                }
                catch (Exception e){
                    return 0;
                }
            }
            
            private String createJson(Map<String,Double> jsonVals){
                /*
                 * creates a simple json list from a map of strings and doubles and converts it to a string
                 */
                StringBuilder json = new StringBuilder("{");
                jsonVals.forEach((name,value)->json.append("\""+name+"\":").append(value).append(","));
                json.deleteCharAt(json.lastIndexOf(","));
                json.append("}");//claude who is JSON???? yo soy programmador de amiente
                return json.toString();
            }
            
            private Response setPIDs(){
                /*
                 * Sets the pids of the shooter module based on URIs sent by the frontend
                 */
                Map<String, List<String>> p = session.getParameters();
                if (!p.containsKey("kPvar") || !p.containsKey("kIvar") || !p.containsKey("kDvar")) {
                    return newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, "Missing kP, kI, or kD");
                }
                try {
                    double kP = getDouble(p,"kPvar");
                    double kI = getDouble(p,"kIvar");
                    double kD = getDouble(p,"kDvar");
                    shooter.setPIDs(kP,kI,kD);
                    return newFixedLengthResponse("OK");
                } catch (Exception e) {
                    return newFixedLengthResponse("Issue Occurred: " + e);
            }
            }

            private Response getData(){
                /*
                 * Sends data from the private static final declared DATALIST
                 * 
                 */
                Map<String, List<String>> p = session.getParameters();
                Response r= newFixedLengthResponse(createJson(WebServer.DATALIST));
                r.addHeader("Content-Type","application/json");
                return r;
            }
            private Response setMotor(){
                /*
                 * this will set the shooter motor to a specific speed based on input parameters for a certain time
                 */
                Map<String, List<String>> p = session.getParameters();
                if (!p.containsKey("value") || !p.containsKey("time")) {
                return newFixedLengthResponse("Missing parameters");
                }
                double setValue = getDouble(p,"value");
                double setTime = getDouble(p,"time");
                CommandScheduler.getInstance().schedule(Commands.run(() -> shooter.setRPM(setValue), shooter).withTimeout(setTime).andThen(shooter::stop));
                
                return newFixedLengthResponse("OK");
            }         
            
            @Override
            public Response serve(IHTTPSession session) {
                WebServer.session = session;

                String uri = WebServer.session.getUri();

                for (String key : uris.keySet()){

                    if (uri.equals(key)){

                        return uris.get(key);
                    }
                }


            try (InputStream is = getClass().getClassLoader().getResourceAsStream("dashboard.html")) {
                if (is == null) return newFixedLengthResponse("dashboard.html wasn't found.");
                return newFixedLengthResponse(Response.Status.OK, "text/html",
                        new String(is.readAllBytes(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                return newFixedLengthResponse("Error");
            }
        }
    }
}