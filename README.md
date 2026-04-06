# expo-rich-input

A native Expo module that replaces `TextInput` entirely, giving the editor raw OS-level edit deltas — insert, delete, and IME compose events — directly from `UIKeyInput` on iOS and `InputConnection` on Android, so the Rope never has to diff a full string.
