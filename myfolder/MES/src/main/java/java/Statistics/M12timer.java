 package Statistics;

import org.OPC_UA.OpcuaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

import java.util.Timer;
import java.util.TimerTask;

public class M12timer {
    public void M12_timer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask(){
            int time=0;


            @Override
            public void run() {
                DataValue data = OpcuaClient.read("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M12.Process.x", 4);
                if (data.getValue().getValue() instanceof Boolean && (Boolean)data.getValue().getValue()){
                    time++;
                }

            }

        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
}

