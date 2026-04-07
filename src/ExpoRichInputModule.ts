import { requireNativeModule } from "expo-modules-core";

const ExpoRichInput = requireNativeModule("ExpoRichInput");

export function focus(viewRef: any): Promise<void> {
    return ExpoRichInput.focus(viewRef);
}

export function blur(viewRef: any): Promise<void> {
    return ExpoRichInput.blur(viewRef);
}
