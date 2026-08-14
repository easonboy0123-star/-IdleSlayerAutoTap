package com.example.idleslayerautotap;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.ArrayDeque;

public final class TargetDetector {
    public static class Hit {
        public final float x, y;
        public final int pixels;
        Hit(float x, float y, int pixels) {
            this.x = x; this.y = y; this.pixels = pixels;
        }
    }

    public static Hit find(Bitmap b, int x1p, int x2p, int y1p, int y2p) {
        int w = b.getWidth(), h = b.getHeight();
        int x1 = Math.max(0, w * x1p / 100);
        int x2 = Math.min(w, w * x2p / 100);
        int y1 = Math.max(0, h * y1p / 100);
        int y2 = Math.min(h, h * y2p / 100);

        // Detect the orange square around the white sword.
        // The scan is deliberately conservative to reduce false positives.
        boolean[][] mask = new boolean[Math.max(1, y2-y1)][Math.max(1, x2-x1)];
        int orangeCount = 0;

        for (int y=y1; y<y2; y++) {
            for (int x=x1; x<x2; x++) {
                int c = b.getPixel(x,y);
                int r = Color.red(c), g = Color.green(c), bl = Color.blue(c);
                boolean orange = r > 165 && g > 65 && g < 190 && bl < 100 && r > g + 55;
                if (orange) {
                    mask[y-y1][x-x1] = true;
                    orangeCount++;
                }
            }
        }

        if (orangeCount < 25) return null;

        boolean[][] seen = new boolean[mask.length][mask[0].length];
        int best = 0, bx=0, by=0, bw=0, bh=0;

        for (int yy=0; yy<mask.length; yy++) {
            for (int xx=0; xx<mask[0].length; xx++) {
                if (!mask[yy][xx] || seen[yy][xx]) continue;

                ArrayDeque<int[]> q = new ArrayDeque<>();
                q.add(new int[]{xx,yy});
                seen[yy][xx] = true;
                int count=0, minX=xx, maxX=xx, minY=yy, maxY=yy;

                while(!q.isEmpty()) {
                    int[] p=q.removeFirst();
                    int px=p[0], py=p[1];
                    count++;
                    minX=Math.min(minX,px); maxX=Math.max(maxX,px);
                    minY=Math.min(minY,py); maxY=Math.max(maxY,py);

                    int[][] ds={{1,0},{-1,0},{0,1},{0,-1}};
                    for(int[] d:ds){
                        int nx=px+d[0], ny=py+d[1];
                        if(nx>=0 && ny>=0 && nx<mask[0].length && ny<mask.length
                                && mask[ny][nx] && !seen[ny][nx]){
                            seen[ny][nx]=true;
                            q.add(new int[]{nx,ny});
                        }
                    }
                }

                int ww=maxX-minX+1, hh=maxY-minY+1;
                if(count>best && ww>=10 && hh>=10 && ww<=180 && hh<=180){
                    best=count; bx=minX; by=minY; bw=ww; bh=hh;
                }
            }
        }

        if(best < 25) return null;

        // Check that the component contains a reasonable amount of near-white pixels.
        int white=0;
        int sx=x1+bx, sy=y1+by;
        int ex=Math.min(x2, sx+bw), ey=Math.min(y2, sy+bh);
        for(int y=sy;y<ey;y++){
            for(int x=sx;x<ex;x++){
                int c=b.getPixel(x,y);
                int r=Color.red(c),g=Color.green(c),bl=Color.blue(c);
                if(r>190 && g>190 && bl>190) white++;
            }
        }

        if(white < Math.max(4, best/10)) return null;

        return new Hit(sx+bw/2f, sy+bh/2f, best);
    }
}