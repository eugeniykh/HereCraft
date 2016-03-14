package Texture;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureCRUD {
	public static Texture loadTexture(String filename, String format) {
		try {
            return TextureLoader.getTexture(format, ResourceLoader.getResourceAsStream("res/textures/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
	}
}
