package net.minecraft.src;

class NetworkMasterThread extends Thread {
    private final NetworkManager networkManager;

    NetworkMasterThread(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000L);
            shutdownThread(NetworkManager.getReadThread(networkManager));
            shutdownThread(NetworkManager.getWriteThread(networkManager));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void shutdownThread(Thread thread) {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            try {
                thread.join(3000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
