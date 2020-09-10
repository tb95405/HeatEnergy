import java.util.Scanner;

public class Main{
	public static void main(String[] args) {
		double mass, startT, endT, totalEnergy;
		String startPhase = "gas";
		String endPhase = "gas";
		boolean endothermic = false;
	
		Scanner input = new Scanner(System.in);
		
		System.out.println("Enter the mass of the water in grams:");
		mass = input.nextDouble();
		
		System.out.println("Enter the intial temperature of the water in Celcius:");
		startT = input.nextDouble();
		//enforce min temp
		if(startT < -273.14) 
			startT = -273.14;
		//phase assignment
		if(startT >= -273.14 && startT <= 0) 
			startPhase = "solid";
		else if(startT > 0  && startT < 100) 
			startPhase = "liquid";
		
		System.out.println("Enter the final temperature of the water in Celcius:");
		endT = input.nextDouble();
		//enforce min temp
		if(endT < -273.14) 
			endT = -273.14;
		//phase assignment
		if(endT >= -273.14 && endT <= 0) 
			endPhase = "solid";
		else if(endT > 0  && endT < 100) 
			endPhase = "liquid";
		
		if (endT > startT) 
			endothermic = true;
		
		System.out.println("Mass: " + mass + " g");
		System.out.println("Starting temperature: " + startT + " C (" + startPhase + ")");
		System.out.println("Ending temperature: " + endT + " C (" + endPhase + ")");
		
		double heatEnergy = 0;
		
		//initially solid
		if(startPhase.equals("solid")) {
			heatEnergy += tempChangeSolid(startT, endT, mass, endPhase, endothermic);
			if(!endPhase.equals("solid")) {
				heatEnergy += fusion(mass, endothermic);
				heatEnergy += tempChangeLiquid(0, endT, mass, endPhase, endothermic);
			}
			if(endPhase.equals("gas")) {
				heatEnergy += vaporization(mass, endothermic);
				heatEnergy += tempChangeGas(100, endT, mass, endPhase, endothermic);
			}
		}//end of solid
		
		//initially liquid
		if(startPhase.equals("liquid")) {
			heatEnergy += tempChangeLiquid(startT, endT, mass, endPhase, endothermic);
			if(endPhase.equals("solid")) {
				heatEnergy += fusion(mass, endothermic);
				heatEnergy += tempChangeSolid(0, endT, mass, endPhase, endothermic);
			}
			if(endPhase.equals("gas")) {
				heatEnergy += vaporization(mass, endothermic);
				heatEnergy += tempChangeGas(100, endT, mass, endPhase, endothermic);
			}
		}//end of liquid
		
		//initially gas
		if(startPhase.equals("gas")) {
			heatEnergy += tempChangeGas(startT, endT, mass, endPhase, endothermic);
			if(!endPhase.equals("gas")) {
				heatEnergy += vaporization(mass, endothermic);
				heatEnergy += tempChangeLiquid(100, endT, mass, endPhase, endothermic);
			}
			if(endPhase.equals("solid")) {
				heatEnergy += fusion(mass, endothermic);
				heatEnergy += tempChangeSolid(0, endT, mass, endPhase, endothermic);
			}
		}//end of gas
				
		
		
		System.out.println("Total Heat Energy: " + round(heatEnergy) + "kJ");
	}
	
	public static double round(double x){
		x *= 1000;
		if (x>0) 
			return (int)(x+0.5)/1000.0;
		else 
			return (int)(x-0.5)/1000.0;
	}
	
	public static double tempChangeSolid(double tempBegin, double tempEnd, double mass, String endPhase, boolean endo) {
		if (!endPhase.equals("solid")) 
			tempEnd = 0; 
		double energyChange = round(mass*0.002108*(tempEnd-tempBegin));
		if(endo) 
			System.out.println("Heating (solid): " + energyChange + "kJ");
		else
			System.out.println("Cooling (solid): " + energyChange + "kJ");	
		return energyChange;
	}
	
	public static double tempChangeLiquid(double tempBegin, double tempEnd, double mass, String endPhase, boolean endo) {
		if (endPhase.equals("gas")) 
			tempEnd = 100; 
		else if(endPhase.equals("solid")) 
			tempEnd = 0; 
		double energyChange = round(mass*0.004184*(tempEnd-tempBegin));
		if(endo) 
			System.out.println("Heating (liquid): " + energyChange + "kJ");
		else
			System.out.println("Cooling (liquid): " + energyChange + "kJ");
		return energyChange;
	}
	
	public static double tempChangeGas(double tempBegin, double tempEnd, double mass, String endPhase, boolean endo) {
		if (!endPhase.equals("gas")) 
			tempEnd = 100;
		double energyChange = round(mass*0.001996*(tempEnd-tempBegin));
		if(endo) 
			System.out.println("Heating (gas): " + energyChange + "kJ");
		else
			System.out.println("Cooling (gas): " + energyChange + "kJ");
		return energyChange;
	}
	public static double fusion(double mass, boolean endo) {
		double energyChange;
		if(endo) {
			energyChange = round(0.333*mass);
			System.out.println("Melting: " + energyChange + "kJ");
		}
		else {
			energyChange = round(-0.333*mass);
			System.out.println("Freezing: " + energyChange + "kJ");
		}	
		return energyChange;
	}
	public static double vaporization(double mass, boolean endo) {
		double energyChange;
		if(endo) {
			energyChange = round(2.257*mass);
			System.out.println("Evaporating: " + energyChange + "kJ");
		}
		else {
			energyChange = round(-2.257*mass);
			System.out.println("Condensing: " + energyChange + "kJ");
		}	
		return energyChange;
		
	}
}
