/**
 * Celestial section of Stellar API.
 * 
 * @author Abastro
 */
@API(apiVersion = "@VERSION@", owner = StellarAPI.modid, provides = StellarAPI.apiid)
package stellarapi.api.celestials;

/**
 * TODO rewrite of the celestial API.
 * 1. Can attain the celestial profile data from the specific region
 * 2. Can attain the celestial objects as renderables from the specific region
 *  - It'll be drawn on specific region and get transformed later
 * 
 * Projection - Transformation & Shader Pair (e.g. Atmospheric)
 *  - Limiting factors are controlled.
 * */

import net.minecraftforge.fml.common.API;
import stellarapi.StellarAPI;