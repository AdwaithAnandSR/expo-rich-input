import { NativeSyntheticEvent } from "react-native";

export type EditEvent = {
    type: "insert" | "delete" | "compose" | "composeCommit";
    text?: string;
    count?: number;
};

export type KeyboardActionEvent = {
    action: string;
};

export type SelectionChangeEvent = {
    start: number;
    end: number;
};

export type ExpoRichInputProps = {
    onEditEvent?: (e: NativeSyntheticEvent<EditEvent>) => void;
    onKeyboardAction?: (e: NativeSyntheticEvent<KeyboardActionEvent>) => void;
    onSelectionChange?: (e: NativeSyntheticEvent<SelectionChangeEvent>) => void;
};

export type ExpoRichInputRef = {
    focus: () => void;
    blur: () => void;
};
