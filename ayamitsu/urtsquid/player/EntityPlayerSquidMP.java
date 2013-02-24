package ayamitsu.urtsquid.player;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ayamitsu.urtsquid.URTSquid;
import ayamitsu.urtsquid.network.PacketHandler;

public class EntityPlayerSquidMP extends EntityPlayerMP {

	public PlayerStatus playerStatus = new PlayerStatus();

	/**
     * add field urts
     */
    protected int air;
    protected int prevAir;

    public float field_70861_d = 0.0F;
    public float field_70862_e = 0.0F;
    public float field_70859_f = 0.0F;
    public float field_70860_g = 0.0F;
    public float field_70867_h = 0.0F;
    public float field_70868_i = 0.0F;

    /** angle of the tentacles in radians */
    public float tentacleAngle = 0.0F;

    /** the last calculated angle of the tentacles in radians */
    public float lastTentacleAngle = 0.0F;
    protected float randomMotionSpeed = 0.0F;
    protected float field_70864_bA = 0.0F;
    protected float field_70871_bB = 0.0F;
    protected float randomMotionVecX = 0.0F;
    protected float randomMotionVecY = 0.0F;
    protected float randomMotionVecZ = 0.0F;

	public EntityPlayerSquidMP(MinecraftServer mcServer, World world, String username, ItemInWorldManager itemInWorldManager) {
		super(mcServer, world, username, itemInWorldManager);
		System.out.println("Spawn Override Player MP:" + username);
		this.texture = "/mob/squid.png";
		this.field_70864_bA = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
		this.setSize(0.95F, 0.95F);
		this.yOffset = 0.0F;
		//this.setPosition(this.posX, this.posY, this.posZ);
		this.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.5F;//0.425F;//this.height * 0.85F;// this.yOffset// 0.12F
	}

	@Override
	protected void resetHeight() {
		super.resetHeight();
		this.setSize(0.95F, 0.95F);
		this.yOffset = 0.0F;
	}

	@Override
	public void preparePlayerToSpawn() {
		this.yOffset = 0.0F;
		this.setSize(0.95F, 0.95F);

		if (this.worldObj != null) {
			while (this.posY > 0.0D) {
				this.setPosition(this.posX, this.posY, this.posZ);

				if (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()) {
					break;
				}

				++this.posY;
			}

			this.motionX = this.motionY = this.motionZ = 0.0D;
			this.rotationPitch = 0.0F;
		}

		this.setEntityHealth(this.getMaxHealth());
		this.deathTime = 0;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public void onUpdate() {
		this.prevAir = this.getAir();
		super.onUpdate();

		if (this.isEntityAlive() && !this.isInsideOfMaterial(Material.water) && !this.isPotionActive(Potion.waterBreathing) && !this.capabilities.disableDamage) {
			this.setAir(this.decreaseAirSupply(this.prevAir));

			if (this.getAir() == -20) {
				this.setAir(0);
				this.attackEntityFrom(DamageSource.drown, 2);
			}
		} else if (this.isInsideOfMaterial(Material.water)) {
			this.setAir(300);
		}

		super.setAir(this.air);
		this.onUpdateSquid();
	}

	public void onUpdateSquid() {
		this.field_70862_e = this.field_70861_d;
		this.field_70860_g = this.field_70859_f;
		this.field_70868_i = this.field_70867_h;
		this.lastTentacleAngle = this.tentacleAngle;
		this.field_70867_h += this.field_70864_bA;

		if (this.field_70867_h > ((float)Math.PI * 2F))
		{
			this.field_70867_h -= ((float)Math.PI * 2F);

			if (this.rand.nextInt(10) == 0)
			{
				this.field_70864_bA = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
			}
		}

		if (this.isInWater())
		{
			float var1;

			if (this.field_70867_h < (float)Math.PI)
			{
				var1 = this.field_70867_h / (float)Math.PI;
				this.tentacleAngle = MathHelper.sin(var1 * var1 * (float)Math.PI) * (float)Math.PI * 0.25F;

				if ((double)var1 > 0.75D)
				{
					this.randomMotionSpeed = 1.0F;
					this.field_70871_bB = 1.0F;
				}
				else
				{
					this.field_70871_bB *= 0.8F;
				}
			}
			else
			{
				this.tentacleAngle = 0.0F;
				this.randomMotionSpeed *= 0.9F;
				this.field_70871_bB *= 0.99F;
			}

			/*if (!this.worldObj.isRemote)
			{
				this.motionX = (double)(this.randomMotionVecX * this.randomMotionSpeed);
				//this.motionY = (double)(this.randomMotionVecY * this.randomMotionSpeed);
				this.motionZ = (double)(this.randomMotionVecZ * this.randomMotionSpeed);
			}*/

			var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.renderYawOffset += (-((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float)Math.PI - this.renderYawOffset) * 0.1F;
			//this.rotationYaw = this.renderYawOffset;
			this.field_70859_f += (float)Math.PI * this.field_70871_bB * 1.5F;
			this.field_70861_d += (-((float)Math.atan2((double)var1, this.motionY)) * 180.0F / (float)Math.PI - this.field_70861_d) * 0.1F;
		}
		else
		{
			this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.field_70867_h)) * (float)Math.PI * 0.25F;

			/*if (!this.worldObj.isRemote)
			{
				this.motionX = 0.0D;
				//this.motionY -= 0.08D;
				//this.motionY *= 0.9800000190734863D;
				this.motionZ = 0.0D;
			}*/

			this.field_70861_d = (float)((double)this.field_70861_d + (double)(-90.0F - this.field_70861_d) * 0.02D);
		}
	}

	@Override
	public void onDeath(DamageSource damage) {
		if (this.playerStatus.getHeartCount() < this.playerStatus.getMaxHeartCount() - 1) {
			this.playerStatus.setHeartCount((byte)(1 + this.playerStatus.getHeartCount()));
			this.setEntityHealth(this.getMaxHealth());
		} else {
			this.playerStatus.setHeartCount((byte)0);
			this.setAir(300);
			super.onDeath(damage);
		}

		PacketHandler.sendStatusToClient(this);
	}

	@Override
	public void setAir(int i) {
		this.air = i;
	}

	@Override
	public float getCurrentPlayerStrVsBlock(Block block) {
		return super.getCurrentPlayerStrVsBlock(block) * (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this) ? 5.0F : 1.0F);
	}

	public float getCurrentPlayerStrVsBlock(Block block, int meta) {
		return super.getCurrentPlayerStrVsBlock(block, meta) * (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this) ? 5.0F : 1.0F);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.air = this.getAir();
		this.playerStatus.readStatus(nbttagcompound.getCompoundTag("URTS.status"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.setAir(this.air);
		super.writeEntityToNBT(nbttagcompound);
		NBTTagCompound statusNBT = new NBTTagCompound();
		this.playerStatus.writeStatus(statusNBT);
		nbttagcompound.setCompoundTag("URTS.status", statusNBT);
	}

}
