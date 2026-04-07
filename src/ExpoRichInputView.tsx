import { requireNativeViewManager } from "expo-modules-core";
import { forwardRef, useRef, useImperativeHandle } from "react";
import { StyleSheet } from "react-native";
import { focus, blur } from "./ExpoRichInputModule";
import { ExpoRichInputProps, ExpoRichInputRef } from "./ExpoRichInput.types";

const NativeView = requireNativeViewManager("ExpoRichInput");

export const ExpoRichInputView = forwardRef<
    ExpoRichInputRef,
    ExpoRichInputProps
>(({ onEditEvent, onKeyboardAction }, ref) => {
    const nativeRef = useRef<any>(null);

    useImperativeHandle(ref, () => ({
        focus: () => focus(nativeRef.current),
        blur: () => blur(nativeRef.current)
    }));

    return (
        <NativeView
            ref={nativeRef}
            style={styles.input}
            onEditEvent={(e: any) => onEditEvent(e.nativeEvent)}
            onKeyboardAction={(e: any) => onKeyboardAction?.(e.nativeEvent)}
        />
    );
});

const styles = StyleSheet.create({
    input: {
        position: "absolute",
        top: 0,
        left: 0,
        width: 1,
        height: 1,
        opacity: 0
    }
});
