package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.App;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginController implements IPluginController
{
    public boolean init() {
        try {
            // Try different possible plugin directory locations
            File currentDir = new File("plugins");
            if (!currentDir.exists()) {
                currentDir = new File("../plugins");
            }
            if (!currentDir.exists()) {
                currentDir = new File("microkernel/plugins");
            }
            if (!currentDir.exists()) {
                currentDir = new File("microkernel/microkernel/plugins");
            }

            // Check if plugins directory exists
            if (!currentDir.exists()) {
                System.out.println("Warning: Plugins directory not found. Tried 'plugins', '../plugins', 'microkernel/plugins', and 'microkernel/microkernel/plugins'.");
                System.out.println("Info: No plugins loaded. Application will run without plugins.");
                return true;
            }

            System.out.println("Info: Loading plugins from: " + currentDir.getAbsolutePath());

            // Define a FilenameFilter to include only .jar files
            FilenameFilter jarFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jar");
                }
            };

            String[] plugins = currentDir.list(jarFilter);
            
            // Check if plugins array is null or empty
            if (plugins == null) {
                System.out.println("Error: Could not read plugins directory.");
                return false;
            }
            
            if (plugins.length == 0) {
                System.out.println("Info: No JAR plugins found in '" + currentDir.getAbsolutePath() + "' directory.");
                return true;
            }

            int i;
            URL[] jars = new URL[plugins.length];
            for (i = 0; i < plugins.length; i++)
            {
                File jarFile = new File(currentDir, plugins[i]);
                jars[i] = jarFile.toURI().toURL();
            }
            URLClassLoader ulc = new URLClassLoader(jars, App.class.getClassLoader());
            for (i = 0; i < plugins.length; i++)
            {
                String pluginName = plugins[i].split("\\.")[0];
                System.out.println("Info: Loading plugin: " + pluginName);
                Class<?> pluginClass = Class.forName("br.edu.ifba.inf008.plugins." + pluginName, true, ulc);
                IPlugin plugin = (IPlugin) pluginClass.getDeclaredConstructor().newInstance();
                plugin.init();
            }

            System.out.println("Info: Successfully loaded " + plugins.length + " plugin(s).");
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();

            return false;
        }
    }
}
