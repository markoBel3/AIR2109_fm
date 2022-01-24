package air.foi.hr.tool

class UndoRedo {
    private var undoStack: MutableList<Tool> = mutableListOf()
    private var redoStack: MutableList<Tool> = mutableListOf()

    fun pushState(tool: Tool) {
        redoStack.clear()
        undoStack.add(tool)
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            undoStack.last().undo()
            redoStack.add(undoStack.removeAt(undoStack.lastIndex))
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            redoStack.last().redo()
            undoStack.add(redoStack.removeAt(redoStack.lastIndex))
        }
    }
}