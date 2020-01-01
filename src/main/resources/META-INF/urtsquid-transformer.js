function initializeCoreMod() {
    // Utility
    Opcodes = Java.type("org.objectweb.asm.Opcodes");
    ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");

    // InsnList
    InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    LabelNode = Java.type("org.objectweb.asm.tree.LabelNode");

    // Node
    FieldNode = Java.type("org.objectweb.asm.tree.FieldNode");
    MethodNode = Java.type("org.objectweb.asm.tree.MethodNode");

    // InsnNode
    AbstractInsnNode = Java.type("org.objectweb.asm.tree.AbstractInsnNode");
    InsnNode = Java.type("org.objectweb.asm.tree.InsnNode");
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    FieldInsnNode = Java.type("org.objectweb.asm.tree.FieldInsnNode");
    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
    JumpInsnNode = Java.type("org.objectweb.asm.tree.JumpInsnNode");
    TypeInsnNode = Java.type("org.objectweb.asm.tree.TypeInsnNode");
    LdcInsnNode = Java.type("org.objectweb.asm.tree.LdcInsnNode");

    // Opecodes
    ACC_PUBLIC = Opcodes.ACC_PUBLIC;
    ACC_PRIVATEE = Opcodes.ACC_PRIVATE;

    INVOKESTATIC = Opcodes.INVOKESTATIC;
    INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;
    INVOKESPECIAL = Opcodes.INVOKESPECIAL;

    ALOAD = Opcodes.ALOAD;
    ILOAD = Opcodes.ILOAD;
    FLOAD = Opcodes.FLOAD;
    DLOAD = Opcodes.DLOAD;

    ISTORE = Opcodes.ISTORE;
    FSTORE = Opcodes.FSTORE;

    RETURN = Opcodes.RETURN;
    ARETURN = Opcodes.ARETURN;
    IRETURN = Opcodes.IRETURN;
    DRETURN = Opcodes.DRETURN;

    NEW = Opcodes.NEW;

    LDC = Opcodes.LDC;

    D2F = Opcodes.D2F;

    ACONST_NULL = Opcodes.ACONST_NULL;
    ICONST_0 = Opcodes.ICONST_0;
    FCONST_0 = Opcodes.FCONST_0;

    IFEQ = Opcodes.IFEQ;
    IFNE = Opcodes.IFNE;
    IF_ACMPEQ = Opcodes.IF_ACMPEQ;

    GETFIELD = Opcodes.GETFIELD;
    GETSTATIC = Opcodes.GETSTATIC;

    GOTO = Opcodes.GOTO;

    LABEL = AbstractInsnNode.LABEL;
    METHOD_INSN = AbstractInsnNode.METHOD_INSN;

    return wrapMethodTransformers({
        "EntityType#<clinit>" : {
            "target" : {
                "type" : "METHOD",
                "class" : "net.minecraft.entity.EntityType",
                "methodName" : "<clinit>",
                "methodDesc" : "()V"
            },
            "transformer" : function(methodNode) {
                injectEntityType_clinit(methodNode.instructions);
                return methodNode;
            }
        },
        "PlayerController#createPlayer" : {
            "target" : {
                "type" : "METHOD",
                "class" : "net.minecraft.client.multiplayer.PlayerController",
                "methodName" : "func_199681_a",
                "methodDesc" : "(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/stats/StatisticsManager;Lnet/minecraft/client/util/ClientRecipeBook;)Lnet/minecraft/client/entity/player/ClientPlayerEntity;"
            },
            "transformer" : function(methodNode) {
                injectPlayerController_createPlayer(methodNode.instructions);
                return methodNode;
            }
        },
        "ClientPlayNetHandler#handleSpawnPlayer" : {
            "target" : {
                "type" : "METHOD",
                "class" : "net.minecraft.client.network.play.ClientPlayNetHandler",
                "methodName" : "func_147237_a",
                "methodDesc" : "(Lnet/minecraft/network/play/server/SRespawnPacket;)V"
            },
            "transformer" : function(methodNode) {
                injectClientPlayNetHandler_handleSpawnPlayer(methodNode.instructions);
                return methodNode;
            }
        },
        "PlayerList#createPlayerForUser" : {
            "target" : {
                "type" : "METHOD",
                "class" : "net.minecraft.server.management.PlayerList",
                "methodName" : "func_148545_a",
                "methodDesc" : "(Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/entity/player/ServerPlayerEntity;"
            },
            "transformer" : function(methodNode) {
                injectPlayerList_createPlayerForUser(methodNode.instructions);
                return methodNode;
            }
        },
        "PlayerList#recreatePlayerEntity" : {
            "target" : {
                "type" : "METHOD",
                "class" : "net.minecraft.server.management.PlayerList",
                "methodName" : "func_72368_a",
                "methodDesc" : "(Lnet/minecraft/entity/player/ServerPlayerEntity;Lnet/minecraft/world/dimension/DimensionType;Z)Lnet/minecraft/entity/player/ServerPlayerEntity;"
            },
            "transformer" : function(methodNode) {
                injectPlayerList_recreatePlayerEntity(methodNode.instructions);
                return methodNode;
            }
        },
        "LightTexture#updateLightmap" : {
            "target" : {
                "type" : "METHOD",
                "class" : "net.minecraft.client.renderer.LightTexture",
                "methodName" : "func_205106_a",
                "methodDesc" : "(F)V"
            },
            "transformer" : function(methodNode) {
                injectLightTexture_updateLightmap(methodNode.instructions)
                return methodNode;
            }
        },
//        "IForgeBlock#getFogColor" : {
//            "target" : {
//                "type" : "METHOD",
//                "class" : "net.minecraftforge.common.extensions.IForgeBlock",
//                "methodName" : "getFogColor",
//                "methodDesc" : "(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;F)Lnet/minecraft/util/math/Vec3d;"
//            },
//            "transformer" : function(methodNode) {
//                injectIForgeBlock_getFogColor(methodNode.instructions);
//                return methodNode;
//            }
//        },
    })
}

function injectEntityType_clinit(instructions) {

    var arrayLength = instructions.size();
    var pointLdcPlayer;

    for (var i = 0; i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == LDC && insn instanceof LdcInsnNode) {
            if (insn.cst == "player") {
                pointLdcPlayer = insn;
                print("Found injection point \"LDC \"player\"\" " + insn);
                break;
            }
        }
    }
    if (!pointLdcPlayer) {
        throw "Error: Couldn't find injection point \"LDC \"player\"\"!";
    }

    var pointLdcWidth;

    for (i = instructions.indexOf(pointLdcPlayer); i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == LDC && insn instanceof LdcInsnNode) {
            if (Math.abs(insn.cst - 0.6) < 0.001) {
                pointLdcWidth = insn;
                print("Found injection point \"LDC 0.6\" " + insn);
                break;
            }
        }
    }
    if (!pointLdcWidth) {
        throw "Error: Couldn't find injection point \"LDC \"player\"\"!";
    }

    // insert
    var insertList = new InsnList();
    insertList.add(new LdcInsnNode(0.8));
    insertList.add(new InsnNode(D2F)); // javascriptからはdoubleしかもっていけないのでdouble->float
    insertList.add(new LdcInsnNode(0.6));
    insertList.add(new InsnNode(D2F)); // javascriptからはdoubleしかもっていけないのでdouble->float
    instructions.insertBefore(pointLdcWidth, insertList);

    // remove
    instructions.remove(pointLdcWidth.getNext());
    instructions.remove(pointLdcWidth);
}

function injectPlayerController_createPlayer(instructions) {

    var arrayLength = instructions.size();

    var pointNewInsn;
    for (var i = 0; i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == NEW && insn instanceof TypeInsnNode) {
            if (insn.desc == "net/minecraft/client/entity/player/ClientPlayerEntity") {
                pointNewInsn = insn;
                print("Found injection point \"new\" " + insn);
                break;
            }
        }
    }
    if (!pointNewInsn) {
        throw "Error: Couldn't find injection point \"new\"!";
    }

    var pointInitPlayer;
    for (i = instructions.indexOf(pointNewInsn); i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == INVOKESPECIAL && insn instanceof MethodInsnNode) {
            if (insn.owner == "net/minecraft/client/entity/player/ClientPlayerEntity") {
                if (insn.name == "<init>") {
                    pointInitPlayer = insn;
                    print("Found injection point \"ClientPlayerEntity#<init>\" " + insn);
                    break;
                }
            }
        }
    }
    if (!pointInitPlayer) {
        throw "Error: Couldn't find injection point \"ClientPlayerEntity#<init>\"!";
    }

    // replace
    pointNewInsn.desc = "ayamitsu0321/urtsquid/client/entity/player/ClientSquidPlayerEntity";
    pointInitPlayer.owner = "ayamitsu0321/urtsquid/client/entity/player/ClientSquidPlayerEntity";
}

function injectClientPlayNetHandler_handleSpawnPlayer(instructions) {

    var arrayLength = instructions.size();

    var pointNewInsn;
    for (var i = 0; i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == NEW && insn instanceof TypeInsnNode) {
            if (insn.desc == "net/minecraft/client/entity/player/RemoteClientPlayerEntity") {
                pointNewInsn = insn;
                print("Found injection point \"new\" " + insn);
                break;
            }
        }
    }
    if (!pointNewInsn) {
        throw "Error: Couldn't find injection point \"new\"!";
    }

    var pointInitPlayer;
    for (i = instructions.indexOf(pointNewInsn); i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == INVOKEVIRTUAL && insn instanceof MethodInsnNode) {
            if (insn.owner == "net/minecraft/client/entity/player/RemoteClientPlayerEntity") {
                if (insn.name == "<init>") {
                    pointInitPlayer = insn;
                    print("Found injection point \"RemoteClientPlayerEntity#<init>\" " + insn);
                    break;
                }
            }
        }
    }
    if (!pointInitPlayer) {
        throw "Error: Couldn't find injection point \"RemoteClientPlayerEntity#<init>\"!";
    }

    // replace
    pointNewInsn.desc = "ayamitsu0321/urtsquid/client/entity/player/RemoteSquidPlayerEntity";
    pointInitPlayer.owner = "ayamitsu0321/urtsquid/client/entity/player/RemoteSquidPlayerEntity";
}

function injectPlayerList_createPlayerForUser(instructions) {

    var arrayLength = instructions.size();

    var pointNewInsn;
    for (var i = 0; i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == NEW && insn instanceof TypeInsnNode) {
            if (insn.desc == "net/minecraft/entity/player/ServerPlayerEntity") {
                pointNewInsn = insn;
                print("Found injection point \"new\" " + insn);
                break;
            }
        }
    }
    if (!pointNewInsn) {
        throw "Error: Couldn't find injection point \"new\"!";
    }

    var pointInitPlayer;
    for (i = instructions.indexOf(pointNewInsn); i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == INVOKESPECIAL && insn instanceof MethodInsnNode) {
            if (insn.owner == "net/minecraft/entity/player/ServerPlayerEntity") {
                if (insn.name == "<init>") {
                    pointInitPlayer = insn;
                    print("Found injection point \"ServerPlayerEntity#<init>\" " + insn);
                    break;
                }
            }
        }
    }
    if (!pointInitPlayer) {
        throw "Error: Couldn't find injection point \"ServerPlayerEntity#<init>\"!";
    }

    // replace
    pointNewInsn.desc = "ayamitsu0321/urtsquid/entity/player/ServerSquidPlayerEntity";
    pointInitPlayer.owner = "ayamitsu0321/urtsquid/entity/player/ServerSquidPlayerEntity";
}

function injectPlayerList_recreatePlayerEntity(instructions) {

    var arrayLength = instructions.size();

    var pointNewInsn;
    for (var i = 0; i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == NEW && insn instanceof TypeInsnNode) {
            if (insn.desc == "net/minecraft/entity/player/ServerPlayerEntity") {
                pointNewInsn = insn;
                print("Found injection point \"new\" " + insn);
                break;
            }
        }
    }
    if (!pointNewInsn) {
        throw "Error: Couldn't find injection point \"new\"!";
    }

    var pointInitPlayer;
    for (i = instructions.indexOf(pointNewInsn); i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == INVOKESPECIAL && insn instanceof MethodInsnNode) {
            if (insn.owner == "net/minecraft/entity/player/ServerPlayerEntity") {
                if (insn.name == "<init>") {
                    pointInitPlayer = insn;
                    print("Found injection point \"ServerPlayerEntity#<init>\" " + insn);
                    break;
                }
            }
        }
    }
    if (!pointInitPlayer) {
        throw "Error: Couldn't find injection point \"ServerPlayerEntity#<init>\"!";
    }

    // replace
    pointNewInsn.desc = "ayamitsu0321/urtsquid/entity/player/ServerSquidPlayerEntity";
    pointInitPlayer.owner = "ayamitsu0321/urtsquid/entity/player/ServerSquidPlayerEntity";
}

function injectIForgeBlock_getFogColor(instructions) {

    var arrayLength = instructions.size();

    // if(ent.isPotionActive(Effects.WATER_BREATHING)) {
    //     f12 = f12 * 0.3F + 0.6F;
    // }
    var WaterBreathing_name = ASMAPI.mapField("field_185248_t");
    var pointPreviousLabel;
    var pointNextLabel;
    for (var i = 0; i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == GETSTATIC && insn.name == WaterBreathing_name) {
            print("Found point \"get static Effects.WATER_BREATHING\" " + insn);

            for (var j = instructions.indexOf(insn); j >= 0; j--) {
                var insn2 = instructions.get(j);
                if (insn2.getType() == LABEL) {
                    pointPreviousLabel = insn2;
                    print("Found remove start point " + pointPreviousLabel);
                    break;
                }
            }

            for (var j = instructions.indexOf(insn); j < arrayLength; j++) {
                var insn2 = instructions.get(j);
                if (insn2.getType() == LABEL) {
                    pointNextLabel = instructions.get(j - 1);
                    print("Found remove end point " + pointNextLabel);
                    break;
                }
            }

            break;
        }
    }
    if (!pointPreviousLabel || !pointNextLabel) {
        throw "Error: Couldn't find injection point \"getFogColor\"!";
    }

    // remove
    var startIndex = instructions.indexOf(pointPreviousLabel);
    var endIndex = instructions.indexOf(pointNextLabel);
    for (i = startIndex; i <= endIndex; i++) {
        instructions.remove(instructions.get(startIndex));
    }
}

function injectLightTexture_updateLightmap(instructions) {

    var arrayLength = instructions.size();


    // float f3 = this.client.player.getWaterBrightness();
    var getWaterBrightness_name = ASMAPI.mapMethod("func_203719_J"); // getWaterBrightness
    var pointInvokeGetWaterBrightness;
    var pointVarFStoreWaterBrightness;
    for (var i = 0; i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == INVOKEVIRTUAL && insn instanceof MethodInsnNode) {
            if (insn.name == getWaterBrightness_name) {
                pointInvokeGetWaterBrightness = insn;
                print("Found point invoke \"getWaterBrightness\" "+ insn);
                break;
            }
        }
    }
    if (!pointInvokeGetWaterBrightness) {
        throw "Error: Couldn't find point invoke \"getWaterBrightness\"!";
    }
    for (var i = instructions.indexOf(pointInvokeGetWaterBrightness); i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == FSTORE) {
            pointVarFStoreWaterBrightness = insn;
            print("Found point invoke \"getWaterBrightness\" "+ insn);
            break;
        }
    }
    if (!pointVarFStoreWaterBrightness) {
        throw "Error: Couldn't find point fstore \"getWaterBrightness\"!";
    }

    // } else if (f3 > 0.0F && this.client.player.isPotionActive(Effects.CONDUIT_POWER)) {
    var CONDUIT_POWER_name = ASMAPI.mapField("field_205136_C");
    var pointGetStatic;
    var pointIfLabel;
    for (var i = instructions.indexOf(pointVarFStoreWaterBrightness); i < arrayLength; i++) {
        var insn = instructions.get(i);
        if(insn.getOpcode() == GETSTATIC && insn.name == CONDUIT_POWER_name){
            pointGetStatic = insn;
            print("Found point get static \"Effects.CONDUIT_POWER\" "+ insn);
            break;
        }
    }
    if (!pointGetStatic) {
        throw "Error: Couldn't find point get static \"Effects.CONDUIT_POWER\"!";
    }
    for (var i = instructions.indexOf(pointGetStatic); i < arrayLength; i++) {
        var insn = instructions.get(i);
        if(insn.getType() == LABEL){
            pointIfLabel = insn;
            print("Found point if label "+ insn);
            break;
        }
    }
    if (!pointIfLabel) {
        throw "Error: Couldn't find point if label!";
    }

    // } else if (f3 > 0.0F && this.client.player.isPotionActive(Effects.CONDUIT_POWER)) {
    //     f2 = f3;  <-here
    // } else {
    var pointVarFStoreBlightness;
    for (var i = instructions.indexOf(pointIfLabel); i < arrayLength; i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == FSTORE) {
            pointVarFStoreBlightness = insn;
            print("Found point fstore blightness "+ insn);
            break;
        }
    }
    if (!pointVarFStoreBlightness) {
        throw "Error: Couldn't find point fstore blightness!";
    }

    // } else {
    //     f2 = 0.0F;
    //     <- insert point
    // }
    var pointInsert;
    var pointJumpLabel;
    for (var i = instructions.indexOf(pointVarFStoreBlightness); i < arrayLength; ++i) {
        var insn = instructions.get(i);
        if (!pointJumpLabel && insn.getOpcode() == GOTO) {
            pointJumpLabel = insn.label;
        }
        if(insn == pointJumpLabel){
            pointInsert = insn;
            print("Found injection point " + insn);
            break;
        }
    }
    if (!pointInsert) {
        throw "Error: Couldn't find injection point!";
    }

    // } else {
    //     f2 = 0.0F;
    // -- insert start --
    //     if (this.client.player.isInWater()) {
    //         f2 = f3;
    //     }
    // -- insert end --
    // }
    var client_name = ASMAPI.mapField("field_205117_h");
    var player_name = ASMAPI.mapField("field_71439_g");
    var areEyesInFluid_name = ASMAPI.mapMethod("func_208600_a");
    var FluidTags_WATER_name = ASMAPI.mapField("field_206959_a");
    var injectList = new InsnList();

    var pointLabelNext = new LabelNode();
    injectList.add(new VarInsnNode(ALOAD, 0));
    injectList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/LightTexture", client_name, "Lnet/minecraft/client/Minecraft;"));
    injectList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/Minecraft", player_name, "Lnet/minecraft/client/entity/player/ClientPlayerEntity;"));
    injectList.add(new FieldInsnNode(GETSTATIC, "net/minecraft/tags/FluidTags", FluidTags_WATER_name, "Lnet/minecraft/tags/Tag;"));
    injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/entity/player/ClientPlayerEntity", areEyesInFluid_name, "(Lnet/minecraft/tags/Tag;)Z"));
    injectList.add(new JumpInsnNode(IFEQ, pointLabelNext));

    injectList.add(new VarInsnNode(FLOAD, pointVarFStoreWaterBrightness.var));
    injectList.add(new VarInsnNode(FSTORE, pointVarFStoreBlightness.var));
    injectList.add(pointLabelNext);

    instructions.insert(pointInsert, injectList);
}

/**
 * Utility function to wrap all method transformers in class transformers
 *
 * @param {object} transformersObj All the transformers of this coremod.
 * @return {object} The transformersObj with all method transformers wrapped.
 */
function wrapMethodTransformers(transformersObj) {

	for (var transformerObjName in transformersObj) {
		var transformerObj = transformersObj[transformerObjName];

		var target = transformerObj["target"];
		if (!target)
			continue;

		var type = target["type"];
		if (!type || !type.equals("METHOD"))
			continue;

		var clazz = target["class"];
		if (!clazz)
			continue;

		var methodName = target["methodName"];
		if (!methodName)
			continue;

		var mappedMethodName = ASMAPI.mapMethod(methodName);

		var methodDesc = target["methodDesc"];
		if (!methodDesc)
			continue;

		var methodTransformer = transformerObj["transformer"];
		if (!methodTransformer)
			continue;

		var newTransformerObjName = "(Method2ClassTransformerWrapper) " + transformerObjName;
		var newTransformerObj = {
			"target": {
				"type": "CLASS",
				"name": clazz,
			},
			"transformer": makeClass2MethodTransformerFunction(mappedMethodName, methodDesc, methodTransformer)
		};

		transformersObj[newTransformerObjName] = newTransformerObj;
		delete transformersObj[transformerObjName];
	}
	return transformersObj;
}

/**
 * Utility function for making the wrapper class transformer function
 * Not part of {@link #wrapMethodTransformers) because of scoping issues (Nashhorn
 * doesn't support "let" which would fix the issues)
 *
 * @param {string} mappedMethodName The (mapped) name of the target method
 * @param {string} methodDesc The description of the target method
 * @param {methodTransformer} transformer The method transformer function
 * @return {function} A class transformer that wraps the methodTransformer
 */
function makeClass2MethodTransformerFunction(mappedMethodName, methodDesc, methodTransformer) {
	return function(classNode) {
		var methods = classNode.methods;
		for (var i in methods) {
			var methodNode = methods[i];
			if (!methodNode.name.equals(mappedMethodName))
				continue;
			if (!methodNode.desc.equals(methodDesc))
				continue;
			methods[i] = methodTransformer(methodNode);
			break;
		}
		return classNode;
	};
}