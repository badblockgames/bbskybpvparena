package fr.ustaN.adds;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YMLFile {

    private File parents;
    private File path;
    private File file;

    private FileConfiguration configuration;

    public YMLFile(String parentsname, String pathname, String filename){
        this.parents = new File(parentsname);
        if(!this.parents.exists()){
            this.parents.mkdir();
        }

        this.path = new File(this.parents, pathname);
        if(!this.path.exists()){
            this.path.mkdir();
        }

        this.file = new File(this.path, filename+".yml");
        try{
            if(!this.file.exists()){
                this.file.createNewFile();
                this.configuration = YamlConfiguration.loadConfiguration(this.file);
            }else{
                this.configuration = YamlConfiguration.loadConfiguration(this.file);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public YMLFile(String pathname, String filename){

        this.path = new File(pathname);
        if(!this.path.exists()){
            this.path.mkdir();
        }

        this.file = new File(this.path, filename+".yml");
        try{
            if(!this.file.exists()){
                this.file.createNewFile();
                this.configuration = YamlConfiguration.loadConfiguration(this.file);
            }else{
                this.configuration = YamlConfiguration.loadConfiguration(this.file);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public YMLFile(String filename){
        this.file = new File(filename+".yml");
        try{
            if(!this.file.exists()){
                this.file.createNewFile();
                this.configuration = YamlConfiguration.loadConfiguration(this.file);
            }else{
                this.configuration = YamlConfiguration.loadConfiguration(this.file);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static boolean FileExist(String parents, String path, String file){

        if(new File(parents).exists()){

            if(new File(parents, path).exists()){

                if(new File(path, file+".yml").exists()){
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public static boolean FileExist(String path, String file){

            if(new File(path).exists()){

                if(new File(path, file+".yml").exists()){
                    return true;
                }

                return false;
            }
            return false;
    }

    public static boolean FileExist(String file){
            if(new File(file+".yml").exists()){
                return true;
            }

        return false;
    }

    public File getParents(){
        return this.parents;
    }

    public File getPath() {
        return this.path;
    }

    public File getFile() {
        return this.file;
    }

    public void saveFile(){
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfiguration() {
        return this.configuration;
    }

}
