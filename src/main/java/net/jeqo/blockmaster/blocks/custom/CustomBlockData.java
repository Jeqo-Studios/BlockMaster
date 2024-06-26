package net.jeqo.blockmaster.blocks.custom;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;

/**
 * An object that stores the custom block data of a custom block
 */
@Getter @Setter
public class CustomBlockData {
    /**
     * The instrument used in the note block
     */
    private Instrument instrument;
    /**
     * The current note of the note block
     */
    private int note;
    /**
     * Whether the note block is powered or not
     */
    private boolean powered;

    /**
     *                 Created a CustomBlockData object from a given block
     * @param block    The block to create the CustomBlockData object from, type org.bukkit.block.Block
     */
    public CustomBlockData(Block block) {
        this((NoteBlock) block.getBlockData());
        Validate.isTrue(block.getType().equals(Material.NOTE_BLOCK), "The given block is not a noteblock");
    }

    /**
     *                Creates a CustomBlockData object from a given NoteBlock object
     * @param data    The NoteBlock object to create the CustomBlockData object from, type org.bukkit.block.data.type.NoteBlock
     */
    public CustomBlockData(NoteBlock data) {
        this.setInstrument(data.getInstrument());
        this.setNote(data.getNote().getId());
        this.setPowered(data.isPowered());
    }

    /**
     *                      Creates a CustomBlockData object with the specified data
     * @param instrument    The instrument of the note block, type org.bukkit.Instrument
     * @param note          The note of the note block, type int
     * @param powered       If the note block is powered, type boolean
     */
    public CustomBlockData(Instrument instrument, int note, boolean powered) {
        this.setInstrument(instrument);
        this.setNote(note);
        this.setPowered(powered);
    }

    /**
     *                Applies the data to a NoteBlock object
     * @param data    The NoteBlock object to apply the data to, type org.bukkit.block.data.type.NoteBlock
     * @return        The NoteBlock object with the applied data, type org.bukkit.block.data.type.NoteBlock
     */
    public NoteBlock applyData(NoteBlock data) {
        data.setInstrument(this.getInstrument());
        data.setNote(new Note(this.getNote()));
        data.setPowered(this.getPowered());
        return data;
    }

    /**
     *                Compares note block data to the note block data of the block data object
     * @param data    The note block data to compare, type org.bukkit.block.data.type.NoteBlock
     * @return        If the data matches, type boolean
     */
    public boolean compareData(NoteBlock data) {
        return compareData(data.getInstrument(), data.getNote().getId(), data.isPowered());
    }

    /**
     *                    Compares the data of the CustomBlockData object with the specified data
     * @param data        The data to compare, type net.jeqo.blockmaster.blocks.custom.CustomBlockData
     * @return            If the data matches, type boolean
     */
    public boolean compareData(CustomBlockData data) {
        return compareData(data.getInstrument(), data.getNote(), data.getPowered());
    }

    /**
     *                     Compares the data of the CustomBlockData object with the specified data
     * @param instrument   The instrument to compare, type org.bukkit.Instrument
     * @param note         The note to compare, type int
     * @param powered      The powered state to compare, type boolean
     * @return             If the data matches, type boolean
     */
    public boolean compareData(Instrument instrument, int note, boolean powered) {
        return this.getInstrument() == instrument && this.getNote() == note && this.getPowered() == powered;
    }

    /**
     *          Gets the powered state of the note block data
     * @return  The powered state of the note block data, type boolean
     */
    public boolean getPowered() { return this.powered; }

    /**
     *             Returns the string representation of the CustomBlockData object as a String. This is an override of the default toString method
     * @return     The string representation of the CustomBlockData object, type java.lang.String
     */
    @Override
    public String toString() {
        return String.format("CustomBlockData[instrument=%s, note=%s, powered=%s]", this.getInstrument(), this.getNote(), this.getPowered());
    }
}
