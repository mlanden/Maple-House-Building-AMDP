package amdp.house.base;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.visualizer.OOStatePainter;
import burlap.visualizer.ObjectPainter;
import burlap.visualizer.StateRenderLayer;
import burlap.visualizer.Visualizer;


public class HouseBaseVisualizer {
    
    private HouseBaseVisualizer() {
        // do nothing
    }

	public static Visualizer getVisualizer(int width, int height){
		StateRenderLayer r = getRenderLayer(width, height);
		Visualizer v = new Visualizer(r);
		return v;
	}
	
	public static StateRenderLayer getRenderLayer(int width, int height){
		StateRenderLayer r = new StateRenderLayer();
		OOStatePainter oopainter = new OOStatePainter();
		oopainter.addObjectClassPainter(HPoint.CLASS_POINT, new PointPainter(width, height, Color.WHITE, 0));
		oopainter.addObjectClassPainter(HBlock.CLASS_BLOCK, new PointPainter(width, height, Color.BLUE, 0));
		oopainter.addObjectClassPainter(HAgent.CLASS_AGENT, new PointPainter(width, height, Color.RED, 1));
		r.addStatePainter(oopainter);
		return r;
	}
	
	public static class PointPainter implements ObjectPainter{

		protected Color			color;
		protected int			width;
		protected int			height;
		protected int			shape = 0; //0 for rectangle 1 for ellipse
		
		public PointPainter(int width, int height, Color color) {
			this.width = width;
			this.height = height;
			this.color = color;
		}
		
		public PointPainter(int width, int height, Color color, int shape) {
			this.width = width;
			this.height = height;
			this.color = color;
			this.shape = shape;
		}

		@Override
		public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob, float cWidth, float cHeight) {
			//set the color of the object
			g2.setColor(this.color);
			
			float domainXScale = this.width;
			float domainYScale = this.height;
			
			//determine then normalized width
			float width = (1.0f / domainXScale) * cWidth;
			float height = (1.0f / domainYScale) * cHeight;

			float rx = (Integer) ob.get(HPoint.ATT_X)*width;
			float ry = cHeight - height - (Integer) ob.get(HPoint.ATT_Y)*height;
			
			if(this.shape == 0){
				g2.fill(new Rectangle2D.Float(rx, ry, width, height));
			}
			else{
				g2.fill(new Ellipse2D.Float(rx, ry, width, height));
			}
		}
	}
	
	
}
