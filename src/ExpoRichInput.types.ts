export type EditEvent = {
    id: number;
    type: "insert" | "delete";
    text?: string;
    count?: number;
    cursor: number;
};

export type KeyboardActionEvent = {
    id: number;
    action: string;
};

export type SelectionChangeEvent = {
    id: number;
    start: number;
    end: number;
};

export type ExpoRichInputProps = {
    onEditEvent?: (e: EditEvent) => void;
    onKeyboardAction?: (e: KeyboardActionEvent) => void;
    onSelectionChange?: (e: SelectionChangeEvent) => void;
};

export type ExpoRichInputRef = {
    focus: () => void;
    blur: () => void;
};
