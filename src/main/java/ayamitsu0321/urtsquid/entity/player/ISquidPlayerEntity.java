package ayamitsu0321.urtsquid.entity.player;

/**
 * イカプレイヤーの共通インターフェース
 */
public interface ISquidPlayerEntity {

    float getSquidPitch();
    float getPrevSquidPitch();
    float getSquidYaw();
    float getPrevSquidYaw();

    float getTentacleAngle();
    float getLastTentacleAngle();

}
