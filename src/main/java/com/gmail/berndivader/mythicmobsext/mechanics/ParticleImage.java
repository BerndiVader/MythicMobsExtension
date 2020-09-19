package com.gmail.berndivader.mythicmobsext.mechanics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;
import com.gmail.berndivader.mythicmobsext.jboolexpr.MathInterpreter;
import com.gmail.berndivader.mythicmobsext.utils.RandomDouble;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name="particleimage", author="Seyarada")
public class ParticleImage extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill  {
	
	String file;
	String backgroundColor;
	String resize;
	String transform;
	int loop;
	int skip;
	double scale;
	Long interval;
	PlaceholderString scaleAmount;
	Particle particle;
	
	
	public ParticleImage(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		file = mlc.getString(new String[] {"file", "f"}, null);
		scaleAmount = PlaceholderString.of(mlc.getString(new String[] {"scale", "s"}, "4"));
		backgroundColor = mlc.getString(new String[] {"bgcolor", "color", "c"}, "white");
		resize = mlc.getString("resize", "false");
		interval = mlc.getLong("interval", 5);
		skip = mlc.getInteger("skip", 5);
		transform = mlc.getString("transform", "x,0,y");
		particle = Particle.valueOf(mlc.getString(new String[] {"particle", "p"}, "REDSTONE").toUpperCase());
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity p) {
		scale = new RandomDouble(scaleAmount.get(data,p)).rollDouble();
		Location loc = BukkitAdapter.adapt(p.getLocation());
		start(loc);
		return true;
	}
	
	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation aL) {
		AbstractEntity caster = data.getTrigger();
		scale = new RandomDouble(scaleAmount.get(data,caster)).rollDouble();
		Location loc = BukkitAdapter.adapt(aL);
		start(loc);
		return false;
	}
	
	public void start(Location loc) {
		
		if(file.contains(".gif")) {
			// GIF animation
			File dir = new File(Main.getPlugin().getDataFolder().getPath() + "/images/" + file);
			try {
				ArrayList<BufferedImage> frames = getFrames(dir);
				loop = 0;
				int size = frames.size();
				new BukkitRunnable() {
					@Override
					public void run() {
						if (loop < size) {
							draw(frames.get(loop), loc);
							loop ++;
						}
						else {
							this.cancel();
						}
					}
				}.runTaskTimerAsynchronously(Main.getPlugin(), 1L, interval);
				
			} catch (IOException e) {e.printStackTrace();}
			
		}
		else {
			// Normal picture
			BufferedImage img = null;
			File dir = new File(Main.getPlugin().getDataFolder().getPath() + "/images/" + file);
			try {
				img = ImageIO.read(dir);
			} catch (IOException e) {e.printStackTrace();}
			
			if(img != null) draw(img, loc);
		}
	}
	
	public void draw(BufferedImage img, Location loc) {
		if(resize != "false") {
			
			String[] x = resize.split(",");
			int tWidth = Integer.valueOf(x[0]);
			int tHeight = Integer.valueOf(x[1]);
			
			try {
				img = resizeImage(img, tWidth, tHeight);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		double width = img.getWidth()-1;
		double height = img.getHeight()-1;
		@Nullable
		World world = loc.getWorld();
	    
	    int k = 1;
	    for (int i = 0; i < width; i++) {
	    	for (int o = 0; o < height; o++) {
	    		  
	    		  if (k >= skip) {
	    			  k = 1;
		    		  
			    	  int pixel = img.getRGB(i, o);
			    	  int r = (pixel>>16) & 0xff;
			    	  int g = (pixel>>8) & 0xff;
			    	  int b = pixel & 0xff;
			    	  
			    	  int bgr = 255;
			    	  int bgg = 255;
			    	  int bgb = 255;
			    	  if (backgroundColor != "white" && !backgroundColor.contains(",")) {
			    		  bgr = 0;
			    		  bgg = 0;
			    		  bgb = 0;}
			    	  else if (backgroundColor.contains(",")) {
			    		  String[] colors = backgroundColor.split(",");
			    		  bgr = Integer.valueOf(colors[0]);
			    		  bgg = Integer.valueOf(colors[1]);
			    		  bgb = Integer.valueOf(colors[2]);
			    	  }
	
			    	  // Removes white/black pixels to remove the background
			    	  if ( r != bgr && g != bgg && b != bgb )
			    	  {
			    		  double x = (i/scale) - ((width/scale)/2.0);
			    		  double y = (o/scale) - ((height/scale)/2.0);
			    		  
			    		  String t1 = transform.split(",")[0];
			    		  String t2 = transform.split(",")[1];
			    		  String t3 = transform.split(",")[2];
			    		  t1 = t1.replace("x", String.valueOf(x)).replace("y", String.valueOf(y));
			    		  t2 = t2.replace("x", String.valueOf(x)).replace("y", String.valueOf(y));
			    		  t3 = t3.replace("x", String.valueOf(x)).replace("y", String.valueOf(y));
			    		  double result1 = MathInterpreter.parse(t1, new HashMap<>()).eval();
			    		  double result2 = MathInterpreter.parse(t2, new HashMap<>()).eval();
			    		  double result3 = MathInterpreter.parse(t3, new HashMap<>()).eval();
			    		  
			    		  
			    		  Location finalLoc = loc.clone();
			    		  finalLoc.add(result1, result2, result3);
			    		  if(particle==Particle.valueOf("REDSTONE")) {
			    			  Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(r, g, b), 1);
				    	      world.spawnParticle(Particle.REDSTONE, finalLoc, 1, 0, 0, 0, 0, dustOptions);
			    		  } else {
			    			  world.spawnParticle(particle, finalLoc, 1, 0, 0, 0, 0);
			    		  }
			    	  }
			    		  
			    	} else { k ++; }
	    		}
	    	}
	}
	
	
	public ArrayList<BufferedImage> getFrames(File gif) throws IOException {
		ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>();
		try {
		    String[] imageatt = new String[]{
		            "imageLeftPosition",
		            "imageTopPosition",
		            "imageWidth",
		            "imageHeight"
		    };    

		    ImageReader reader = (ImageReader)ImageIO.getImageReadersByFormatName("gif").next();
		    ImageInputStream ciis = ImageIO.createImageInputStream(gif);
		    reader.setInput(ciis, false);

		    int noi = reader.getNumImages(true);
		    BufferedImage master = null;

		    for (int i = 0; i < noi; i++) {
		        BufferedImage image = reader.read(i);
		        IIOMetadata metadata = reader.getImageMetadata(i);
		        
		        Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
		        NodeList children = tree.getChildNodes();

		        for (int j = 0; j < children.getLength(); j++) {
		            Node nodeItem = children.item(j);

		            if(nodeItem.getNodeName().equals("ImageDescriptor")){
		                Map<String, Integer> imageAttr = new HashMap<String, Integer>();

		                for (int k = 0; k < imageatt.length; k++) {
		                    NamedNodeMap attr = nodeItem.getAttributes();
		                    Node attnode = attr.getNamedItem(imageatt[k]);
		                    imageAttr.put(imageatt[k], Integer.valueOf(attnode.getNodeValue()));
		                }
		                if(i==0){
		                    master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"), BufferedImage.TYPE_INT_ARGB);
		                }
		                master.getGraphics().drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"), null);
		            }
		        }
		        
		        // I don't know why, I don't want to know why, I shouldn't have to 
		        // wonder why, but for whatever reason I need to write master
		        // then read it to get the actual value
		        File dir = new File( Main.getPlugin().getDataFolder().getPath() + "/images/qlJ06jLEg8.png");
		        ImageIO.write(master, "GIF", dir);
		        BufferedImage a = ImageIO.read(dir);
		        frames.add(a);
		        dir.delete();
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return frames;
	}
	
	BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
	    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
	    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
	    return outputImage;
	}

}
