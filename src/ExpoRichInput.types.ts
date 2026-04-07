export type EditEventType = "insert" | "delete" | "compose" | "composeCommit";

export interface EditEvent {
    type: EditEventType;
    text?: string;
    count?: number;
}

export interface KeyboardActionEvent {
    action: "undo" | "redo" | "toggleComment";
}

export interface ExpoRichInputRef {
    focus: () => Promise<void>;
    blur: () => Promise<void>;
}

export interface ExpoRichInputProps {
    onEditEvent: (event: EditEvent) => void;
    onKeyboardAction?: (event: KeyboardActionEvent) => void;
}
