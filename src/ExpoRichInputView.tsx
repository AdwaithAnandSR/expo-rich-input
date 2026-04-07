import React, { forwardRef, useImperativeHandle, useRef } from "react";
import { requireNativeViewManager } from "expo-modules-core";
import type {
    ExpoRichInputProps,
    ExpoRichInputRef
} from "./ExpoRichInput.types";

const NativeView = requireNativeViewManager("ExpoRichInput");

const RichInput = forwardRef<ExpoRichInputRef, ExpoRichInputProps>(
    (props, ref) => {
        const nativeRef = useRef<any>(null);

        useImperativeHandle(ref, () => ({
            focus: () => nativeRef.current?.focus(),
            blur: () => nativeRef.current?.blur()
        }));

        return <NativeView ref={nativeRef} {...props} />;
    }
);

export default RichInput;
