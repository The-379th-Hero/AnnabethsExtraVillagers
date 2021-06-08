package annabeth.extravillagers.blocks;

import javax.annotation.Nullable;

import annabeth.extravillagers.RegistryHandler;
import annabeth.extravillagers.container.SawmillContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SawmillBlock extends Block {
	private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent("container.sawmill");
	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);
	
	public SawmillBlock() {
		super(Properties.of(Material.WOOD).strength(2.5f).sound(SoundType.WOOD));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
		return this.defaultBlockState().setValue(FACING, p_196258_1_.getHorizontalDirection().getOpposite());
	}
	
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isClientSide) {
			return super.use(state, world, pos, player, hand, hit);
		} else {
			NetworkHooks.openGui((ServerPlayerEntity) player, state.getMenuProvider(world, pos), pos);
			player.awardStat(RegistryHandler.INTERACT_WITH_SAWMILL);
			return ActionResultType.CONSUME;
		}
	}
	
	@Override
	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
		return new SimpleNamedContainerProvider((id, inventory, player) -> {
			return new SawmillContainer(id, inventory, IWorldPosCallable.create(world, pos));
		}, CONTAINER_NAME);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public boolean isPathfindable(BlockState state, IBlockReader world, BlockPos pos, PathType type) {
		return false;
	}
}