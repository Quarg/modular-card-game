import java.util.HashMap;

import java.io.FileReader;
import java.io.FileNotFoundException;

import javax.json.*;

/*  
    Utility class for providing curves based on JSON files.
    Note that the curves should be a strictly increasing function.
*/

abstract class CurveProvider
{
    private static HashMap<String,CurveInfo> curveCache;

    public static double curveAt(String curveName, double x)
    {
        CurveInfo curve = curveCache.get(curveName);
        if(curve == null)
        {
            curve = CurveInfo.loadCurveFromFile(curveName);
            if(curve == null)
                return 0;
        }
        return curve.evaluateAt(x);
    }
}

class CurveInfo
{
    double[] sampleX;
    double[] sampleY;

    public double evaluateAt(double x)
    {
        if(x > sampleX[0])
        {
            for (int i = 1; i < sampleX.length; i++) 
            {
                if(sampleX[i] == x)
                    return sampleY[i];

                if(sampleX[i] < x)
                {
                    double t = (x - sampleX[i - 1]) / (sampleX[i] - sampleX[i - 1]);
                    return CurveInfo.lerp(sampleY[i - 1], t, sampleY[i]);
                }
            }
            return sampleY[sampleY.length - 1];
        }
        return sampleY[0];
    }

    public static CurveInfo loadCurveFromFile(String curveName)
    {
        try
        {
            JsonReader reader = Json.createReader(new FileReader("data/curves/"+curveName+".json"));
            JsonObject obj = reader.readObject();
            JsonArray xs = obj.getJsonArray("xSamples");
            JsonArray ys = obj.getJsonArray("ySamples");
            
            int size = xs.size();
                //Ignore extra data, messy, but functional.
            if(size > ys.size())
                size = ys.size();

            CurveInfo curve = new CurveInfo();

            curve.sampleX = new double[size];
            curve.sampleY = new double[size];

            for (int i = 0; i < size; i++) 
            {
                curve.sampleX[i] = xs.getJsonNumber(i).doubleValue();
                curve.sampleY[i] = ys.getJsonNumber(i).doubleValue();
            }

            return curve;
        }
        catch(FileNotFoundException e)
        {
            System.out.println("JSON for curve '"+curveName+"' could not be found.");
        }
        return null;
    }

    public static double lerp(double a, double t, double b)
    {
        return (a * (1 - t)) + (b * t);
    }
}