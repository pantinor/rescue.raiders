package rescue.raiders.objects;

import java.io.File;
import java.io.FilenameFilter;
import org.testng.annotations.Test;

public class ObjectsTest {

    @Test
    public void testDirection() throws Exception {

        File dir = new File("assets/audio/ogg");
        File[] oggs = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("ogg");
            }
        });

        for (File f : oggs) {
            System.out.println(f.getName().toUpperCase().replace(".OGG","").replace("-","_") + "(\"" + f.getName()+"\", false, 0.1f),");
        }

    }

}
