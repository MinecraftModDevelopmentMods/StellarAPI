package stellarapi.api.celestials.collection.model;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.celestials.CRequest;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.celestials.collection.ISegment;

public abstract class TypeModel implements ICapabilityProvider {

	private final CelestialType celType;

	// TODO custom dispatcher & event
	private CapabilityIndexedDispatcher capabilities = null;
	private List<IRecognizer> recognizers = Lists.newArrayList();

	public TypeModel(CelestialType type) {
		this.celType = type;
	}

	public abstract void readFrom(ISegment segment, CRequest request);

	public final @Nullable CelestialObject check(ISegment segment) {
		for(IRecognizer recognizer : this.recognizers) {
			if(recognizer.accept(segment, this))
				return recognizer.createObject(segment, this);
		}

		return null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.capabilities != null? capabilities.hasCapability(capability, facing) : false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.capabilities != null? capabilities.getCapability(capability, facing) : null;
	}

}
